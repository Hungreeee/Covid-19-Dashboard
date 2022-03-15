import Main.stage
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.{Node, Scene}
import scalafx.scene.canvas.Canvas
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{Pane, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.Font
import scalafx.stage.{FileChooser, Stage}

class FileHandler extends VBox {
  // stylesheets add getClass.getResource("styles.css").toExternalForm
  val uploadButton = new Button("Upload file")
  var label = new Label("Hello")
  var fReader = new FileReader("")
  uploadButton.onAction = (event) => {
    val fileChooser = new FileChooser
    val file = fileChooser.showOpenDialog(stage)
    if (file != null) {
      uploadButton.text = "Upload a different file"
      val filePath = file.getCanonicalPath
      val fileName = filePath.reverse.takeWhile(_ != '\\').reverse
      label = new Label("Uploaded file : " + fileName)
      if(filePath.endsWith(".json")) {
        fReader = new FileReader(filePath)
      }
      else {
        label = new Label("File is not compatible! File type must be .json")
        this.children = Iterable[Node](uploadButton, label)
      }
      this.children = Iterable[Node](uploadButton, label)
    }
  }
  this.children = Iterable[Node](uploadButton, label)
  def getData = fReader.getData
}

