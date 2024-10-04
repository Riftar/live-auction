package com.riftar.liveauctionapp.liveauction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riftar.liveauctionapp.domain.liveauction.model.AuctionItem
import com.riftar.liveauctionapp.domain.liveauction.model.BidDetail
import com.riftar.liveauctionapp.domain.liveauction.model.LiveComment
import com.riftar.liveauctionapp.domain.liveauction.model.StreamModel
import com.riftar.liveauctionapp.domain.liveauction.repository.CommentRepository
import com.riftar.liveauctionapp.domain.liveauction.repository.LiveAuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LiveAuctionViewModel @Inject constructor(
    private val repository: LiveAuctionRepository,
    private val commentRepository: CommentRepository
) :
    ViewModel() {
    val stream: StateFlow<StreamModel> = MutableStateFlow(
        StreamModel(
            avatarUrl = "https://picsum.photos/id/${
                Random.nextInt(1, 100)
            }/200/200",
            streamID = "stream19",
            userName = "CollectorLegend!!11!",
            viewCount = 34567,
            streamTitle = "The Most EPIC SALE!!1!",
            streamDate = 1667776000000, // April 2022
            thumbnailUrl = "https://picsum.photos/id/${
                Random.nextInt(1, 100)
            }/1080/1920/?blur=1"
        )
    )

    private val _auctionItem = MutableStateFlow<AuctionItem?>(null)
    val auctionItem: StateFlow<AuctionItem?> = _auctionItem
    private val _timeRemaining = MutableStateFlow(10)
    val timeRemaining: StateFlow<Int> = _timeRemaining
    private val _itemIndex = MutableStateFlow(0)
    val itemIndex: StateFlow<Int> = _itemIndex
    private val _bidHistory = MutableStateFlow(listOf<BidDetail>())
    val bidHistory: StateFlow<List<BidDetail>> = _bidHistory
    private val _listComment = MutableStateFlow(listOf<LiveComment>())
    val listComment: StateFlow<List<LiveComment>> = _listComment

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
            val bid = BidDetail(userName, currentItem.currentPrice.toDouble() + 5)
            repository.placeBid(currentItem.id, bid)
        }
    }

    fun getBidHistory(itemId: String) {
        viewModelScope.launch {
            repository.getBidHistory(itemId).collect { bidHistory ->
                _bidHistory.value = bidHistory
            }
        }
    }

    fun goToNextItem() {
        viewModelScope.launch {
            // Max index is 2 for now
            val currentIndex = if (_itemIndex.value == 2) {
                -1
            } else _itemIndex.value

            _itemIndex.emit(currentIndex + 1)
        }
    }

    fun sendComment(userName: String, comment: String) {
        viewModelScope.launch {
            commentRepository.sendComment(
                LiveComment(
                    author = userName,
                    comment = comment
                )
            )
        }
    }

    fun getListComment() {
        viewModelScope.launch {
            commentRepository.getComments().collect { listComment ->
                _listComment.value = listComment
            }
        }
    }
}
