package com.riftar.liveauctionapp.liveauction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riftar.liveauctionapp.domain.liveauction.model.AuctionItem
import com.riftar.liveauctionapp.domain.liveauction.model.Bid
import com.riftar.liveauctionapp.domain.liveauction.repository.LiveAuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LiveAuctionViewModel @Inject constructor(private val repository: LiveAuctionRepository) :
    ViewModel() {
    val stream: StateFlow<StreamModel> = MutableStateFlow(
        StreamModel(
            avatarUrl = "https://picsum.photos/id/${
                Random.nextInt(1, 100)
            }/200/200",
            streamID = "stream19",
            userName = "NaturePhotographer",
            viewCount = 34567,
            streamTitle = "Nature Photography Tips",
            streamDate = 1667776000000, // April 2022
            thumbnailUrl = "https://picsum.photos/id/${
                Random.nextInt(1, 100)
            }/1080/1920/?blur=1"
        )
    )
    private val listComments = listOf(
        Comment(
            id = 1,
            author = "Sheryl",
            comment = "The base ScrollScope where the scroll session was created.",
            avatar = R.drawable.ic_avatar_1,
            date = 3021
        ),
        Comment(
            id = 2,
            author = "Adele",
            comment = "logic describing fling behavior.",
            avatar = R.drawable.ic_avatar_3,
            date = 3021
        ),
        Comment(
            id = 3,
            author = "Robert",
            comment = "An implementation of LazyLayoutScrollScope that works with LazyRow and LazyColumn.",
            avatar = R.drawable.ic_avatar_2,
            date = 3021
        ),
        Comment(
            id = 4,
            author = "Adele",
            comment = "the vertical alignment applied to the items",
            avatar = R.drawable.ic_avatar_3,
            date = 3021
        ),
        Comment(
            id = 5,
            author = "Sheryl",
            comment = "the state object to be used to control or observe the list's state",
            avatar = R.drawable.ic_avatar_1,
            date = 3021
        ),
        Comment(
            id = 6,
            author = "Robert",
            comment = "a factory of the content types for the item. The item compositions of the same type could be reused more efficiently. Note that null is a valid type and items of such type will be considered compatible.",
            avatar = R.drawable.ic_avatar_2,
            date = 3021
        ),
    )
    private val doubleList = mutableListOf<Comment>().apply {
        repeat(10) {
            addAll(listComments)
        }
    }
    val listComment = MutableStateFlow(doubleList)
    val listBid = MutableStateFlow(
        listOf(
            BidDetail(
                id = 1,
                avatar = R.drawable.ic_avatar_1,
                userName = "Adele",
                bidAmount = 25,
                date = 3021
            ),
            BidDetail(
                id = 2,
                avatar = R.drawable.ic_avatar_3,
                userName = "Raymond",
                bidAmount = 35,
                date = 3021
            ),
            BidDetail(
                id = 1,
                avatar = R.drawable.ic_avatar_2,
                userName = "Michael",
                bidAmount = 45,
                date = 3021
            )
        )
    )
    private val _auctionItem = MutableStateFlow<AuctionItem?>(null)
    val auctionItem: StateFlow<AuctionItem?> = _auctionItem
    private val _timeRemaining = MutableStateFlow(10)
    val timeRemaining: StateFlow<Int> = _timeRemaining
    private val _itemIndex = MutableStateFlow(0)
    val itemIndex: StateFlow<Int> = _itemIndex

    fun getAuctionItem() {
        viewModelScope.launch {
            combine(_itemIndex, repository.getAuctionItemFlow()) { index, auctionItems ->
                auctionItems[index]
            }.collect { auctionItem ->
                _auctionItem.value = auctionItem
            }
        }
    }

    fun getTimeRemaining() {
        viewModelScope.launch {
            repository.getTimeRemaining().collect { time ->
                _timeRemaining.value = time
            }
        }
    }

    fun placeBid(userName: String, currentItem: AuctionItem) {
        viewModelScope.launch {
            val bid = Bid(userName, currentItem.currentPrice.toDouble() + 5)
            repository.placeBid(currentItem.id, bid)
        }
    }

    fun goToNextItem() {
        viewModelScope.launch {
            // Max index is 2 for now
            val currentIndex = if (_itemIndex.value > 2) {
                0
            } else _itemIndex.value

            _itemIndex.emit(currentIndex + 1)
        }
    }
}


data class StreamModel(
    val avatarUrl: String,
    val streamID: String,
    val userName: String,
    val viewCount: Int,
    val streamTitle: String,
    val streamDate: Long,
    val thumbnailUrl: String
)

data class Comment(
    val id: Int,
    val author: String,
    val comment: String,
    val avatar: Int,
    val date: Long
)

data class BidDetail(
    val id: Int,
    val avatar: Int,
    val userName: String,
    val bidAmount: Int,
    val date: Long
)