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
  // initiate components
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

  // create shadow effect
  val dropShadow = new DropShadow()
  dropShadow.setRadius(5.0)
  dropShadow.setSpread(0)
  dropShadow.setOffsetX(0.0)
  dropShadow.setOffsetY(1.0)
  dropShadow.setColor(Color.color(0, 0, 0, 0.20))
  val clearShadow = new DropShadow()
  clearShadow.setColor(Color.color(0, 0, 0, 0))

  // link components
  buttonContainer.children = Array(uploadButton)
  labelContainer.children = label
  contentContainer.children = Array(buttonContainer, labelContainer, dateChoose, hideButton)
  this.children = Array(contentContainer)

  // align and style components
  contentContainer.alignment = Pos.Center
  labelContainer.alignment = Pos.Center
  buttonContainer.alignment = Pos.Center
  this.alignment = Pos.Center
  contentContainer.spacing = 10
  labelContainer.spacing = 5
  buttonContainer.spacing = 10
  contentContainer.minWidth = Screen.primary.visualBounds.width / 3
  contentContainer.minHeight = Screen.primary.visualBounds.height / 2.8

  label.setFont(Font.font("", FontWeight.Bold, 10))
  labelWarn1.setFont(Font.font("", FontWeight.Medium, 10))
  labelWarn1.setTextFill(Color.Gray)
  labelWarn2.setFont(Font.font("", FontWeight.Medium, 10))
  labelWarn2.setTextFill(Color.Gray)

  // set up and style hide-show button
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

  // set up Upload button
  uploadButton.onAction = (event) => {
    // create file chooser
    val fileChooser = new FileChooser
    val file = fileChooser.showOpenDialog(stage)
    // if a file is chosen
    if (file != null) {
      // get file's information
      val filePath = file.getCanonicalPath
      val fileName = filePath.reverse.takeWhile(_ != '\\').reverse

      // style components
      uploadButton.text = "Upload a different file"
      label.text = "Uploaded file : " + fileName

      // if .json file
      if(filePath.endsWith(".json")) {
        // get data
        val data = new JsonFileReader(filePath)
        // update components' state when file is .json
        label.textFill = Color.Black

        buttonContainer.children = Array(uploadButton, saver)
        labelContainer.children = label
        diagram.children = new Diagram(data.getData.reverse, "06/09/2021") // 6/9/2021 is default date
        table.children = new DataTable(data.getData.reverse, "06/09/2021")
        saver.children = new FileSaver(data.getData.reverse, "06/09/2021")
        dateChoose.children = new dateChooser(data.getData.reverse)

        dateChoose.visible = true
        diagram.visible = true
        saver.visible = true
        label.visible = true
        table.visible = true
      }
      // if .hihi file
      else if(filePath.endsWith(".hihi")) {
        // get data
        val data = new HihiFileReader(filePath)
        val displayData = data.getData

        // if data is valid
        if(displayData.size == 7) {
          val displayDate = displayData.head._1
          // update compoents when file is .hihi
          buttonContainer.children = Array(uploadButton)
          diagram.children = new Diagram(displayData, displayDate)
          table.children = new DataTable(displayData, displayDate)
          labelContainer.children = Array(label, labelWarn1, labelWarn2)

          label.textFill = Color.Black
          label.setText("Displaying Dashboard saved in file " + fileName)

          dateChoose.visible = false
          saver.visible = false
          diagram.visible = true
          label.visible = true
          table.visible = true
        }
        // handle (intentionally modified) faulty .hihi file
        else {
          labelContainer.children = label
          label.textFill = Color.Red
          label.text = "File " + fileName + " is not appropriate! Please upload another file"
          label.visible = true
        }
      }
      // handle wrong file type
      else {
        labelContainer.children = label
        label.textFill = Color.Red
        label.text = "File " + fileName + " is not appropriate! Please only upload .json or .hihi file"
        label.visible = true
      }
    }
  }
  // style component
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
  // initiate helper def
  def dateExist(d: String) = data.map(_._1).contains(d)
  def getMinDate = if(data.nonEmpty) data.head._1 else ""
  def getMaxDate = if(data.nonEmpty) data.last._1 else ""
  def get7Day(d: String) = {
    if ((data.map(_._1).indexOf(d) + 6 < data.size) && data.map(_._1).indexOf(d) != -1) {
      data.map(_._1).apply(data.map(_._1).indexOf(d) + 6)
    }
    else ""
  }

  // initiate components
  val label1 = new Label("The data will be displayed as a 7-day interval. Input start date here.")
  val label2 = new Label("Earliest: " + this.getMinDate + " Latest: " + this.getMaxDate)
  val dateInput = new CustomInput("Date")
  val monthInput = new CustomInput("Month")
  val yearInput = new CustomInput("Year")
  val submitButton = new Button("Submit")
  val errorLabel = new Label("Error")
  val inputContainer = new HBox()

  // style components
  errorLabel.visible = false
  label2.setTextFill(Color.Gray)
  this.spacing = 5

  // handle submit date button clicks
  submitButton.onAction = (event) => {
    // format string date so they will be the format "dd/mm/yy"
    val formatDate = (if(dateInput.getText.length == 1) "0" + dateInput.getText
                       else dateInput.getText) + "/" + (if(monthInput.getText.length == 1) "0" + monthInput.getText else monthInput.getText) + "/" + yearInput.getText

    // handle different inputs
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
    // if input is valid
    else {
      // update components state with the new date
      diagram.children = new Diagram(data, formatDate)
      table.children = new DataTable(data, formatDate)
      saver.children = new FileSaver(data, formatDate)

      errorLabel.setTextFill(Color.Black)
      errorLabel.text = "End date: " + this.get7Day(formatDate)
      errorLabel.setFont(Font.font("", FontWeight.Bold, 10))
      errorLabel.visible = true
    }
  }

  // style, align and link components
  inputContainer.spacing = 3
  inputContainer.alignment = Pos.Center
  this.alignment = Pos.Center

  inputContainer.children = Array(dateInput, monthInput, yearInput, submitButton)
  this.children = Array(label1, label2, inputContainer, errorLabel)

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

}

class CustomInput(placeholder: String) extends TextField {
  // create customed text field
  this.promptText = placeholder
  this.maxWidth = 70
  this.setBorder(new Border(new BorderStroke(Color.White,
            BorderStrokeStyle.Solid, new CornerRadii(0), BorderWidths.Default)))
  this.setBackground(new Background(Array(new BackgroundFill(Color.White, new CornerRadii(0), Insets(0)))))
  // helper def
  def isNum = this.getText.forall(_.isDigit) && this.getText != ""
  def toNum = this.getText.toInt
}

class JsonFileReader(filePath: String) {
  var j = 0
  var extractedData = Seq[Tuple4[String, Int, Int, Int]]()
  if(filePath != "") {
    val file = scala.io.Source.fromFile(filePath)
    var linesInFile = file.getLines.toSeq
      .filterNot(i => {i.contains('{') || i.contains("},") || i.contains(']') || i.contains('}') || i.contains("[")})
      .map(i => i.replaceAll(" ", "")).map(i => i.replaceAll(",", ""))
      linesInFile.foreach(i => {
        if(i.contains("Finland")) {
          extractedData = extractedData :+ Tuple4(linesInFile(j - 6).replaceAll("\"dateRep\":", "").replaceAll("\"", ""), linesInFile(j - 2).replaceAll("\"cases\":", "").replaceAll("\"", "").toInt,
            linesInFile(j - 1).replaceAll("\"deaths\":", "").replaceAll("\"", "").toInt, linesInFile(j + 3).replaceAll("\"popData2020\":", "").replaceAll("\"", "").toInt)
        }
        j += 1
      })
  }
  def getData =extractedData
}

class HihiFileReader(filePath: String) {
  var extractedData = Seq[Tuple4[String, Int, Int, Int]]()
  // flag to detect faulty files
  var flag = true
  if(filePath != "") {
    val file = scala.io.Source.fromFile(filePath)
    var linesInFile = file.getLines.toSeq
      .map(i => i.replaceAll(" ", ""))
      .filterNot(_ == "")
    for(i <- linesInFile.indices by 4) {
      if(i + 3 < linesInFile.size && linesInFile(i + 1).forall(_.isDigit) && linesInFile(i + 2).forall(_.isDigit)
        && linesInFile(i + 3).forall(_.isDigit) && linesInFile(i).replaceAll("/", "").forall(_.isDigit)) {
        extractedData = extractedData :+ Tuple4(linesInFile(i), linesInFile(i + 1).toInt, linesInFile(i + 2).toInt, linesInFile(i + 3).toInt)
      }
      else {
        flag = false
      }
    }
  }
  def getData = if(flag) extractedData else Seq[Tuple4[String, Int, Int, Int]]()
}

class FileSaver(data: Seq[Tuple4[String, Int, Int, Int]], date: String) extends Button {
  // find display date
  val displayedDate = data.slice(data.map(_._1).indexOf(date), data.map(_._1).indexOf(date)+7)

  // style and create buttons functioning
  this.minHeight = 45
  this.minWidth = 130
  this.setBackground(new Background(Array(new BackgroundFill(Color.Orange, new CornerRadii(0), Insets(0)))))
  this.setText("Save current dashboard state")
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
  this.onAction = (e) => {
    // save file if clicked
    val fileChooser = new FileChooser
    fileChooser.setTitle("Save diagram's current state")
    fileChooser.setInitialFileName(date.replaceAll("/", "") + "Dashboard") // get initial name for files (for convenience)
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

