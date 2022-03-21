import Main.stage
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.embed.swing.SwingFXUtils
import scalafx.geometry.Pos.TopCenter
import scalafx.scene.{Node, Scene}
import scalafx.scene.canvas.Canvas
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{Border, BorderPane, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, HBox, Pane, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.{Font, FontWeight}
import scalafx.stage.{FileChooser, Screen, Stage}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.paint.Color

class DataTable(data: Seq[Tuple4[String, Int, Int, Int]], date: String) extends VBox {

  val hideButton = new Button("Hide")
  val showButton = new Button("Show")
  val contentContainer = new VBox()

  this.children = contentContainer
  this.padding = Insets(10)

  this.alignment = Pos.Center
  contentContainer.alignment = Pos.Center

  contentContainer.minWidth = Screen.primary.visualBounds.width / 2.5
  contentContainer.minHeight = Screen.primary.visualBounds.height / 3.5

  hideButton.onAction = (event) => {
    this.children = showButton
  }
  showButton.onAction = (event) => {
    this.children = contentContainer
  }

  if(data.nonEmpty) {
    val title = Label("Displaying the data of the period (" + date + " to " + data.map(_._1).apply(data.map(_._1).indexOf(date) + 6)
      + (if(date == "06/09/2021") " (Default)" else "") + ")")
    title.setFont(new Font(15))
    val displayedDate = data.slice(data.map(_._1).indexOf(date), data.map(_._1).indexOf(date)+7)
    val container = new HBox()
    val cardsContainer = new VBox()
    val calcContainer = new VBox()

    val sumDeathVal = displayedDate.map(i => i._3).sum
    val sumCasesVal = displayedDate.map(i => i._2).sum
    val sumDeathContainer = new Cards("Total death cases", sumDeathVal.toString)
    val sumCasesContainer = new Cards("Total confirmed cases", sumCasesVal.toString)
    val population = displayedDate.size*1.0
    val averageCasesVal = (math floor (((sumCasesVal*1.00)/7.00)*100)/100)
    val averageDeathsVal = (math floor (((sumDeathVal*1.00)/7.00)*100)/100)
    val averageCasesLabel = new Label("Average confirmed cases per day: " + averageCasesVal.toString)
    val averageDeathsLabel = new Label("Average deaths per day: " + averageDeathsVal.toString)
    val minCasesLabel = new Label("Minimum confirmed cases: " + displayedDate.map(i => i._2).min)
    val maxCasesLabel = new Label("Maximum confirmed cases: " + displayedDate.map(i => i._2).max)
    val minDeathsLabel = new Label("Minimum death cases: " + displayedDate.map(i => i._3).min)
    val maxDeathsLabel = new Label("Maximum death cases: " + displayedDate.map(i => i._3).max)
    val casesSD = new Label("Standard deviation of confirmed cases: " + ((math floor (Math.sqrt(displayedDate.map(i => Math.pow(i._2-averageCasesVal, 2)).sum/7))*100.0)/100.0).toString)
    val deathSD = new Label("Standard deviation of confirmed cases: " + ((math floor (Math.sqrt(displayedDate.map(i => Math.pow(i._3-averageDeathsVal, 2)).sum/7))*100.0)/100.0).toString)

    cardsContainer.spacing = 10

    cardsContainer.children = Array(sumDeathContainer, sumCasesContainer)
    calcContainer.children = Array(averageCasesLabel, averageDeathsLabel, minCasesLabel, maxCasesLabel, minDeathsLabel, maxDeathsLabel, casesSD, deathSD)
    container.children = Array(cardsContainer, calcContainer)
    container.spacing = 20
    container.alignment = Pos.Center
    contentContainer.children = Array(title, container, hideButton)
    contentContainer.spacing = 10
    this.children = contentContainer
  }
}

class Cards(title: String, text: String) extends VBox {
  this.setBorder(new Border(new BorderStroke(Color.Black,
            BorderStrokeStyle.Solid, new CornerRadii(5.0), BorderWidths.Default)))
  this.padding = Insets(5)
  this.alignment = Pos.Center
  val titleLabel = new Label(title)
  titleLabel.setFont(new Font(15))
  val textLabel = new Label(text)
  textLabel.setFont(new Font(20))
  println(javafx.scene.text.Font.getFamilies)
  this.children = Array(titleLabel, textLabel)
}
