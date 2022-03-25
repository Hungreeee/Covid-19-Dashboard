import Main.{diagram, stage, table, saver}
import scalafx.scene.control._
import scalafx.scene.layout.{Background, BackgroundFill, Border, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, HBox, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.{Font, FontWeight}
import scalafx.stage.{FileChooser, Screen}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.TextField
import scalafx.scene.effect.DropShadow
import scalafx.scene.paint.Color
import java.io.{File, FileOutputStream, PrintWriter}
import scala.collection.mutable


class FileHandler() extends VBox {
  val label = new Label("")
  val labelWarn1 = new Label("Please note that the program will only display the 7-day interval saved in the file")
  val labelWarn2 = new Label("Some functionality will not be available as they no longer make sense")
  val uploadButton = new Button("Upload file")
  val hideButton = new Button("Hide")
  val showButton = new Button("Show")
  var dateChoose = new dateChooser(Seq[Tuple4[String, Int, Int, Int]]())
  val contentContainer = new VBox()
  val buttonContainer = new HBox()
  val labelContainer = new VBox()

  dateChoose.visible = false
  label.visible = false
  saver.visible = false

  buttonContainer.children = Array(uploadButton)
  labelContainer.children = label
  contentContainer.children = Array(buttonContainer, labelContainer, dateChoose, hideButton)
  this.children = Array(contentContainer)

  contentContainer.alignment = Pos.Center
  labelContainer.alignment = Pos.Center
  contentContainer.spacing = 10
  this.alignment = Pos.Center
  labelContainer.spacing = 5
  contentContainer.minWidth = Screen.primary.visualBounds.width / 3
  contentContainer.minHeight = Screen.primary.visualBounds.height / 2.8
  buttonContainer.alignment = Pos.Center
  buttonContainer.spacing = 10

  label.setFont(Font.font("", FontWeight.Bold, 10))
  labelWarn1.setFont(Font.font("", FontWeight.Medium, 10))
  labelWarn1.setTextFill(Color.Gray)
  labelWarn2.setFont(Font.font("", FontWeight.Medium, 10))
  labelWarn2.setTextFill(Color.Gray)

  val dropShadow = new DropShadow()
  dropShadow.setRadius(5.0)
  dropShadow.setSpread(0)
  dropShadow.setOffsetX(0.0)
  dropShadow.setOffsetY(1.0)
  dropShadow.setColor(Color.color(0, 0, 0, 0.20))
  val clearShadow = new DropShadow()
  clearShadow.setColor(Color.color(0, 0, 0, 0))

  hideButton.setBackground(new Background(Array(new BackgroundFill(Color.White, new CornerRadii(0), Insets(0)))))
  hideButton.setTextFill(Color.Black)
  hideButton.setFont(Font.font("", FontWeight.Bold, 10))

  hideButton.onMouseEntered = (e) => {
    hideButton.setEffect(dropShadow)
    hideButton.setTextFill(Color.Orange)
  }
  hideButton.onMouseExited = (e) => {
    hideButton.setEffect(clearShadow)
    hideButton.setTextFill(Color.Black)
  }

  showButton.setBackground(new Background(Array(new BackgroundFill(Color.White, new CornerRadii(0), Insets(0)))))
  showButton.setTextFill(Color.Black)
  showButton.setFont(Font.font("", FontWeight.Bold, 10))
  showButton.onMouseEntered = (e) => {
    showButton.setEffect(dropShadow)
    showButton.setTextFill(Color.Orange)
  }
  showButton.onMouseExited = (e) => {
    showButton.setEffect(clearShadow)
    showButton.setTextFill(Color.Black)
  }

  hideButton.onAction = (event) => {
    this.children = showButton
  }
  showButton.onAction = (event) => {
    this.children = contentContainer
  }

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
        buttonContainer.children = Array(uploadButton, saver)
        labelContainer.children = label
        diagram.children = new Diagram(data.getData.reverse, "06/09/2021")
        table.children = new DataTable(data.getData.reverse, "06/09/2021")
        saver.children = new FileSaver(data.getData.reverse, "06/09/2021")
        dateChoose.children = new dateChooser(data.getData.reverse)
        dateChoose.visible = true
        diagram.visible = true
        saver.visible = true
        label.visible = true
        table.visible = true
      }
      else if(filePath.endsWith(".hihi")) {
        val data = new CustomFileReader(filePath)
        val displayData = data.getData
        if(displayData.size == 7) {
          val displayDate = displayData.head._1
          buttonContainer.children = Array(uploadButton)
          diagram.children = new Diagram(displayData, displayDate)
          table.children = new DataTable(displayData, displayDate)
          label.setText("Displaying Dashboard saved in file " + fileName)
          labelContainer.children = Array(label, labelWarn1, labelWarn2)
          dateChoose.visible = false
          saver.visible = false
          diagram.visible = true
          label.visible = true
          table.visible = true
        }
        else {
          labelContainer.children = label
          label.text = "File " + fileName + " is not appropriate! Please upload another file"
          label.visible = true
        }
      }
      else {
        labelContainer.children = label
        label.text = "File " + fileName + " is not appropriate! Please only upload .json or .hihi file"
        label.visible = true
      }
    }
  }
  // styles
  uploadButton.minHeight = 45
  uploadButton.minWidth = 130
  uploadButton.setBackground(new Background(Array(new BackgroundFill(Color.Orange, new CornerRadii(0), Insets(0)))))
  uploadButton.setTextFill(Color.White)
  uploadButton.setFont(Font.font("", FontWeight.Bold, 12))
  uploadButton.onMouseEntered = (e) => {
    uploadButton.setBackground(new Background(Array(new BackgroundFill(Color.White, new CornerRadii(0), Insets(0)))))
    uploadButton.setTextFill(Color.Black)
  }
  uploadButton.onMouseExited = (e) => {
    uploadButton.setBackground(new Background(Array(new BackgroundFill(Color.Orange, new CornerRadii(0), Insets(0)))))
    uploadButton.setTextFill(Color.White)
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
  val label1 = new Label("The data will be displayed as a 7-day interval. Input start date here.")
  val label2 = new Label("Earliest: " + this.getMinDate + " Latest: " + this.getMaxDate)
  val dateInput = new CustomInput("Date")
  val monthInput = new CustomInput("Month")
  val yearInput = new CustomInput("Year")
  val submitButton = new Button("Submit")
  val errorLabel = new Label("Error")
  errorLabel.visible = false
  label2.setTextFill(Color.Gray)
  this.spacing = 5
  submitButton.onAction = (event) => {
    val formatDate = (if(dateInput.getText.length == 1) "0" + dateInput.getText
                       else dateInput.getText) + "/" + (if(monthInput.getText.length == 1) "0" + monthInput.getText else monthInput.getText) + "/" + yearInput.getText
    if(!dateInput.isNum || !monthInput.isNum || !yearInput.isNum) {
      errorLabel.setFont(Font.font("", FontWeight.Medium, 10))
      errorLabel.text = "Input is not a number!"
      errorLabel.setTextFill(Color.Red)
      errorLabel.visible = true
    }
    else if((dateInput.toNum > 31) || (monthInput.toNum > 12) ||  (yearInput.toNum < 0)) {
      errorLabel.setFont(Font.font("", FontWeight.Medium, 10))
      errorLabel.text = "Date is invalid"
      errorLabel.setTextFill(Color.Red)
      errorLabel.visible = true
    }
    else if(!this.dateExist(formatDate)) {
      errorLabel.setFont(Font.font("", FontWeight.Medium, 10))
      errorLabel.text = "Cannot find that date! " + "Earliest: " + this.getMinDate + " Latest: " + this.getMaxDate
      errorLabel.setTextFill(Color.Red)
      errorLabel.visible = true
    }
    else if(this.get7Day(formatDate) == "") {
      errorLabel.setFont(Font.font("", FontWeight.Medium, 10))
      errorLabel.text = "Start date is invalid. Please note that it must be 7 days back from the lastest date. " + "Earliest: " + this.getMinDate + " Latest: " + this.getMaxDate
      errorLabel.setTextFill(Color.Red)
      errorLabel.visible = true
    }
    else {
      diagram.children = new Diagram(data, formatDate)
      table.children = new DataTable(data, formatDate)
      saver.children = new FileSaver(data, formatDate)
      errorLabel.setTextFill(Color.Black)
      errorLabel.text = "End date: " + this.get7Day(formatDate)
      errorLabel.setFont(Font.font("", FontWeight.Bold, 10))
      errorLabel.visible = true
    }
  }

  val inputContainer = new HBox()
  inputContainer.children = Array(dateInput, monthInput, yearInput, submitButton)
  inputContainer.spacing = 3
  inputContainer.alignment = Pos.Center

  submitButton.setBackground(new Background(Array(new BackgroundFill(Color.Orange, new CornerRadii(0), Insets(0)))))
  submitButton.setTextFill(Color.White)
  submitButton.setFont(Font.font("", FontWeight.Bold, 12))
  submitButton.onMouseEntered = (e) => {
    submitButton.setBackground(new Background(Array(new BackgroundFill(Color.White, new CornerRadii(0), Insets(0)))))
    submitButton.setTextFill(Color.Black)
  }
  submitButton.onMouseExited = (e) => {
    submitButton.setBackground(new Background(Array(new BackgroundFill(Color.Orange, new CornerRadii(0), Insets(0)))))
    submitButton.setTextFill(Color.White)
  }

  this.alignment = Pos.Center
  this.children = Array(label1, label2, inputContainer, errorLabel)
}

class CustomInput(placeholder: String) extends TextField {
  this.promptText = placeholder
  this.maxWidth = 70
  this.setBorder(new Border(new BorderStroke(Color.White,
            BorderStrokeStyle.Solid, new CornerRadii(0), BorderWidths.Default)))
  this.setBackground(new Background(Array(new BackgroundFill(Color.White, new CornerRadii(0), Insets(0)))))
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

class CustomFileReader(filePath: String) {
  var extractedData = Seq[Tuple4[String, Int, Int, Int]]()
  if(filePath != "") {
    val file = scala.io.Source.fromFile(filePath)
    var linesInFile = file.getLines.toSeq
      .map(i => i.replaceAll(" ", ""))
      .filterNot(_ == "")
    for(i <- linesInFile.indices by 4) {
      extractedData = extractedData :+ Tuple4(linesInFile(i), linesInFile(i+1).toInt, linesInFile(i+2).toInt, linesInFile(i+3).toInt)
    }
  }
  def getData = extractedData
}

class FileSaver(data: Seq[Tuple4[String, Int, Int, Int]], date: String) extends Button {
  this.minHeight = 45
  this.minWidth = 130
  this.setBackground(new Background(Array(new BackgroundFill(Color.Orange, new CornerRadii(0), Insets(0)))))
  this.setTextFill(Color.White)
  this.setFont(Font.font("", FontWeight.Bold, 12))
  this.onMouseEntered = (e) => {
    this.setBackground(new Background(Array(new BackgroundFill(Color.White, new CornerRadii(0), Insets(0)))))
    this.setTextFill(Color.Black)
  }
  this.onMouseExited = (e) => {
    this.setBackground(new Background(Array(new BackgroundFill(Color.Orange, new CornerRadii(0), Insets(0)))))
    this.setTextFill(Color.White)
  }
  val displayedDate = data.slice(data.map(_._1).indexOf(date), data.map(_._1).indexOf(date)+7)
  this.setText("Save current dashboard state")
  this.onAction = (e) => {
    val fileChooser = new FileChooser
    fileChooser.setTitle("Save diagram's current state")
    fileChooser.setInitialFileName(date.replaceAll("/", "") + "Dashboard")
    fileChooser.getExtensionFilters.addAll(new FileChooser.ExtensionFilter("Hihi file", "*.hihi"))
    val file = fileChooser.showSaveDialog(stage)

    if(file != null) {
    val fileContent = mutable.Buffer[String]()
    val printWriter = new PrintWriter(new FileOutputStream(new File(file.getCanonicalPath)))
      for(i <- displayedDate) {
        printWriter.write(i._1 + "\n" + i._2.toString + "\n" + i._3.toString + "\n" + i._4.toString + "\n")
      }
      printWriter.close()
    }
  }
}

