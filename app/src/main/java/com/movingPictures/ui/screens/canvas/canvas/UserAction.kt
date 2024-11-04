import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.movingPictures.ui.screens.canvas.CanvasViewModel
import com.movingPictures.ui.screens.canvas.ControlTool
import com.movingPictures.ui.screens.canvas.PlayState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun onCurrentTool(viewModel: CanvasViewModel): Modifier {
    val tool = viewModel.currentTool.collectAsState()
    val playState = viewModel.gifPlayer.playState.collectAsState()

    val penActionController = viewModel.penController
    val eraserActionController = viewModel.eraserController
    val moveActionController = viewModel.moveController

    fun onDrawUpdate(offset: Offset) {
        if (playState.value == PlayState.PLAYING) return
        when (tool.value) {
            ControlTool.PEN -> penActionController.onDrawUpdate(offset)
            ControlTool.ERASER -> eraserActionController.onDrawUpdate(offset)
            ControlTool.MOVE -> moveActionController.onDrawUpdate(offset)
            else -> {}
        }
    }

    fun onCancel() {
        if (playState.value == PlayState.PLAYING) return
        when (tool.value) {
            ControlTool.PEN -> penActionController.onCancel()
            ControlTool.ERASER -> eraserActionController.onCancel()
            ControlTool.MOVE -> moveActionController.onCancel()
            else -> {}
        }
    }

    return Modifier.pointerInteropFilter { event ->
        val offset = Offset(event.x, event.y)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onDrawUpdate(offset)
                true
            }

            MotionEvent.ACTION_MOVE -> {
                onDrawUpdate(offset)
                true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                onCancel()
                true
            }

            else -> false
        }
    }
}
