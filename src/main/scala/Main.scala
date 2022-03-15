import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.control._
import scalafx.scene.layout.{GridPane, Pane, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.Font
import scalafx.stage.{FileChooser, Stage}


object Main extends JFXApp {
  val root = new GridPane
  val scene = new Scene(root)
  stage = new JFXApp.PrimaryStage {
    title.value = "Data Dashboard"
  }
  stage.scene = scene
  stage.setResizable(false)
  stage.setMaximized(true)

  val uploadButton = new Button("Upload file")
  var label = new Label("")
  var diagram = new Diagram(Seq[Tuple4[String, Int, Int, Int]]())
  var table = new DataTable(Seq[Tuple4[String, Int, Int, Int]]())
  diagram.visible = false
  label.visible = false
  table.visible = false

  root.children += uploadButton
  root.children += label
  root.children += diagram
  root.children += table

  uploadButton.onAction = (event) => {
    val fileChooser = new FileChooser
    val file = fileChooser.showOpenDialog(stage)
    if (file != null) {
      uploadButton.text = "Upload a different file"
      val filePath = file.getCanonicalPath
      val fileName = filePath.reverse.takeWhile(_ != '\\').reverse
      label.text = "Uploaded file : " + fileName
      val checkBox = new CheckBox("TestBox")
      if(filePath.endsWith(".json")) {
        val data = new FileReader(filePath)
        diagram.children = new Diagram(data.getData)
        table.children = new DataTable(data.getData)
        diagram.visible = true
        label.visible = true
        table.visible = true
      }
      else {
        label.text = "File " + fileName + " is not appropriate! Please upload .json file only"
        label.visible = true
      }
    }
  }
}

class FileReader(filePath: String) {
  var j = 0
  var extractedData = Seq[Tuple4[String, Int, Int, Int]]()
  if(filePath != "") {
    val file = scala.io.Source.fromFile(filePath)
    var linesInFile = file.getLines.toSeq
      .filterNot(i => {i.contains('{') || i.contains("},") || i.contains(']') || i.contains('}') || i.contains("[")})
      .map(i => i.replaceAll(" ", "")).map(i => i.replaceAll(",", ""))
      linesInFile.foreach(i => {
        if (i.contains("Finland"))
          extractedData = extractedData :+ Tuple4(linesInFile(j-6), linesInFile(j-2).replaceAll("\"cases\":", "").replaceAll("\"", "").toInt,
            linesInFile(j-1).replaceAll("\"deaths\":", "").replaceAll("\"", "").toInt, linesInFile(j+3).replaceAll("\"popData2020\":", "").replaceAll("\"", "").toInt)
        j += 1
      } )
  }
  def getData = extractedData
}
