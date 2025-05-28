package com.systems.notchi.presentation.frames

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.systems.notchi.domain.model.FrameModel
import com.systems.notchi.domain.model.TaskModel
import com.systems.notchi.domain.usecase.frames.AddFrameListUseCase
import com.systems.notchi.domain.usecase.frames.AddFrameUseCase
import com.systems.notchi.domain.usecase.frames.DeleteFrameListUseCase
import com.systems.notchi.domain.usecase.frames.DeleteFrameUseCase
import com.systems.notchi.domain.usecase.frames.GetFramesUseCase
import com.systems.notchi.domain.usecase.tasks.AddTaskListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class FrameViewModel(
    private val getAllFramesUseCase: GetFramesUseCase,
    private val addFrameUseCase: AddFrameUseCase,
    private val deleteFrameUseCase: DeleteFrameUseCase,
    private val addFrameListUseCase: AddFrameListUseCase,
    private val addTaskListUseCase: AddTaskListUseCase,
    private val deleteFrameListUseCase: DeleteFrameListUseCase,
) : ViewModel() {

    private val _frames = MutableStateFlow<List<FrameModel>>(emptyList())
    val frames: StateFlow<List<FrameModel>> = _frames.asStateFlow()

    private val _selectionMode = MutableStateFlow(false)
    val selectionMode: StateFlow<Boolean> = _selectionMode

    private val _selectedFrames = MutableStateFlow<Set<String>>(emptySet())
    val selectedFrames: StateFlow<Set<String>> = _selectedFrames


    fun getAllFrames() {
        viewModelScope.launch {
            try {
                val result = getAllFramesUseCase.execute()
                _frames.value = result
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error loading frames", e)
            }
        }
    }
    suspend fun addFrame(frame: FrameModel) {
        viewModelScope.launch {
            addFrameUseCase.execute(frame)
            _frames.value += frame
        }
    }

    fun toggleSelection(uid: String) {
        val currentSelected = _selectedFrames.value.toMutableSet()
        if (currentSelected.contains(uid)) {
            currentSelected.remove(uid)
        } else {
            currentSelected.add(uid)
        }
        _selectedFrames.value = currentSelected

        // 🧠 If no frames are selected, disable selection mode
        if (currentSelected.isEmpty()) {
            disableSelectionMode()
        }
    }

    fun disableSelectionMode() {
        _selectionMode.value = false
    }

    fun enableSelectionMode() {
        _selectionMode.value = true
    }

    fun clearSelection() {
        _selectedFrames.value = emptySet()
        _selectionMode.value = false
    }

    suspend fun deleteSelectedFrames() {
        val framesToDelete = _selectedFrames.value.mapNotNull { id ->
            _frames.value.find { it.uid == id }
        }
        if (framesToDelete.isNotEmpty()) {
            deleteFrameListUseCase.execute(framesToDelete)
            getAllFrames()
        }
        clearSelection()
    }

    suspend fun addSelectedFrames() {
        val framesToAdd = _selectedFrames.value.mapNotNull { id ->
            _frames.value.find { it.uid == id }
        }

        if (framesToAdd.isNotEmpty()) {
            addFrameListUseCase.execute(framesToAdd)
            getAllFrames()
        }
        clearSelection()
    }

    suspend fun addSelectedTasks(selectedDate: Long) {
        val framesToAdd = _selectedFrames.value.mapNotNull { id ->
            _frames.value.find { it.uid == id }
        }

        val tasksToAdd = framesToAdd.map { frame ->
            TaskModel(
                title = frame.title,
                checked = false,
                subTasks = emptyList(),
                date = Date(selectedDate),
                points = frame.points,
                project = frame.project
            )
        }

        if (tasksToAdd.isNotEmpty()) {
            addTaskListUseCase.execute(tasksToAdd)
            getAllFrames()
        }
        clearSelection()
    }
}