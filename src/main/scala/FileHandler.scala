import Main.{diagram, stage, table}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.{Cursor, Node, Scene}
import scalafx.scene.canvas.Canvas
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, Pane, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.Font
import scalafx.stage.{FileChooser, Screen, Stage}
import scalafx.geometry.{Orientation, Pos}
import scalafx.scene.control.TextField
import scalafx.scene.input.MouseEvent


class FileHandler() extends VBox {
  val label = new Label("")
  val uploadButton = new Button("Upload file")
  val hideButton = new Button("Hide")
  val showButton = new Button("Show")
  var dateChoose = new dateChooser(Seq[Tuple4[String, Int, Int, Int]]())
  val contentContainer = new VBox()

  dateChoose.visible = false
  label.visible = false

  contentContainer.children = Array(uploadButton, label, dateChoose, hideButton)
  this.children = Array(contentContainer)
  contentContainer.alignment = Pos.Center
  this.alignment = Pos.Center
  contentContainer.minWidth = Screen.primary.visualBounds.width / 3
  contentContainer.minHeight = Screen.primary.visualBounds.height / 3.5

  hideButton.onAction = (event) => {
    this.children = showButton
  }
  showButton.onAction = (e) => {
    this.children = contentContainer
  }
  uploadButton.minHeight = 50
  uploadButton.minWidth = 400
  uploadButton.layoutX = 290
  uploadButton.layoutY = 400
  uploadButton.onAction = (event) => {
    val fileChooser = new FileChooser
    val file = fileChooser.showOpenDialog(stage)
    if (file != null) {
      uploadButton.text = "Upload a different version of the file"
      val filePath = file.getCanonicalPath
      val fileName = filePath.reverse.takeWhile(_ != '\\').reverse
      label.text = "Uploaded file : " + fileName
      val checkBox = new CheckBox("TestBox")
      if(filePath.endsWith(".json")) {
        val data = new FileReader(filePath)
        diagram.children = new Diagram(data.getData.reverse, "06/09/2021")
        table.children = new DataTable(data.getData.reverse, "06/09/2021")
        dateChoose.children = new dateChooser(data.getData.reverse)
        dateChoose.visible = true
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

class dateChooser(data: Seq[Tuple4[String, Int, Int, Int]]) extends VBox {
  def dateExist(d: String) = data.map(_._1).contains(d)
  def getMinDate = if(data.nonEmpty) data.head._1 else ""
  def getMaxDate = if(data.nonEmpty) data.last._1 else ""
  def get7Day(d: String) = {
    if ((data.map(_._1).indexOf(d) + 6 < data.size) && data.map(_._1).indexOf(d) != -1) {
      data.map(_._1).apply(data.map(_._1).indexOf(d) + 6)
    }
    else ""
  }
  val label = new Label("The data will be displayed as a 7-day interval. Input start date here." + "\n" + "Earliest: " + this.getMinDate + " Latest: " + this.getMaxDate)
  val dateInput = new CustomInput("Date")
  val monthInput = new CustomInput("Month")
  val yearInput = new CustomInput("Year")
  val submitButton = new Button("Submit")
  val errorLabel = new Label("Error")
  errorLabel.visible = false
  submitButton.onAction = (event) => {
    val formatDate = (if(dateInput.getText.length == 1) "0" + dateInput.getText
    else dateInput.getText) + "/" + (if(monthInput.getText.length == 1) "0" + monthInput.getText else monthInput.getText) + "/" + yearInput.getText
    if(!dateInput.isNum || !monthInput.isNum || !yearInput.isNum) {
      errorLabel.text = "Input is not a number!"
      errorLabel.visible = true
    }
    else if((dateInput.toNum > 31) || (monthInput.toNum > 12) ||  (yearInput.toNum < 0)) {
      errorLabel.text = "Date is invalid"
      errorLabel.visible = true
    }
    else if(!this.dateExist(formatDate)) {
      errorLabel.text = "Cannot find that date!" + "\n" + "Earliest: " + this.getMinDate + " Latest: " + this.getMaxDate
      errorLabel.visible = true
    }
    else if(this.get7Day(formatDate) == "") {
      errorLabel.text = "Start date is invalid. Please note that it must be 7 days back from the lastest date."+ "\n" + "Earliest: " + this.getMinDate + " Latest: " + this.getMaxDate
      errorLabel.visible = true
    }
    else {
      diagram.children = new Diagram(data, formatDate)
      table.children = new DataTable(data, formatDate)
      errorLabel.text = "End date: " + this.get7Day(formatDate)
      errorLabel.visible = true
    }
  }

  val inputContainer = new HBox()
  inputContainer.children = Array(dateInput, monthInput, yearInput)
  inputContainer.spacing = 3
  inputContainer.alignment = Pos.Center

  this.alignment = Pos.Center
  this.children = Array(label, inputContainer, errorLabel, submitButton)
}

class CustomInput(placeholder: String) extends TextField {
  this.promptText = placeholder
  this.maxWidth = 70
  def isNum = this.getText.forall(_.isDigit) && this.getText != ""
  def toNum = this.getText.toInt
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
          extractedData = extractedData :+ Tuple4(linesInFile(j-6).replaceAll("\"dateRep\":", "").replaceAll("\"", ""), linesInFile(j-2).replaceAll("\"cases\":", "").replaceAll("\"", "").toInt,
            linesInFile(j-1).replaceAll("\"deaths\":", "").replaceAll("\"", "").toInt, linesInFile(j+3).replaceAll("\"popData2020\":", "").replaceAll("\"", "").toInt)
        j += 1
      } )
  }
  def getData = extractedData
}

