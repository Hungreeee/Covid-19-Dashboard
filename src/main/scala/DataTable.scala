import scalafx.Includes._
import scalafx.scene.control._
import scalafx.scene.layout.{Background, BackgroundFill, Border, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, HBox, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.{Font, FontWeight}
import scalafx.stage.Screen
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.effect.DropShadow
import scalafx.scene.paint.Color

class DataTable(data: Seq[Tuple4[String, Int, Int, Int]], date: String) extends VBox {
  // initiate components
  val hideButton = new Button("Hide")
  val showButton = new Button("Show")
  val contentContainer = new VBox()
  this.children = contentContainer

  // create shadow effect
  val dropShadow = new DropShadow()
  dropShadow.setRadius(5.0)
  dropShadow.setSpread(0)
  dropShadow.setOffsetX(0.0)
  dropShadow.setOffsetY(1.0)
  dropShadow.setColor(Color.color(0, 0, 0, 0.20))
  val clearShadow = new DropShadow()
  clearShadow.setColor(Color.color(0, 0, 0, 0))

  // align and style components
  this.alignment = Pos.Center
  contentContainer.alignment = Pos.Center
  contentContainer.minWidth = Screen.primary.visualBounds.width / 2.05
  contentContainer.minHeight = Screen.primary.visualBounds.height / 2.8

  // set hide-show button's hover-exit event handler and styles
  hideButton.onAction = (event) => {
    this.setAlignment(Pos.Center)
    this.children = showButton
  }
  showButton.onAction = (event) => {
    this.setAlignment(Pos.Center)
    this.children = contentContainer
  }
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
  // only show components when receive legit data - otherwise do nothing
  if(data.nonEmpty) {
    val title = Label("Calculations of the data of the period (" + date + " to " + data.map(_._1).apply(data.map(_._1).indexOf(date) + 6)
      + (if(date == "06/09/2021") " (Default)" else "") + ")")
    title.setFont(new Font(17))
    // initiate components
    val displayedDate = data.slice(data.map(_._1).indexOf(date), data.map(_._1).indexOf(date)+7)
    val container = new HBox()
    val cardsContainer = new VBox()
    val calcContainer = new HBox()
    val calcContainer1 = new VBox()
    val calcContainer2 = new VBox()
    val calcContainer3 = new VBox()
    val calcContainer4 = new VBox()
    val titleContainer = new HBox()

    // perform calculations (min, max, sum, std devi, average)
    val sumDeathVal = displayedDate.map(i => i._3).sum
    val sumCasesVal = displayedDate.map(i => i._2).sum
    val sumDeathContainer = new Cards("total death cases", sumDeathVal.toString)
    val sumCasesContainer = new Cards("total confirmed cases", sumCasesVal.toString)
    val population = displayedDate.size*1.0
    val averageCasesVal = (Math.round(((sumCasesVal*1.00)/7.00)*100.00)/100.00)
    val averageDeathsVal = (Math.round(((sumDeathVal*1.00)/7.00)*100.00)/100.00)
    // add calculated values to labels
    val averageCasesLabel = new SmallCards("Average confirmed", averageCasesVal.toString)
    val averageDeathsLabel = new SmallCards("Average death", averageDeathsVal.toString)
    val minCasesLabel = new SmallCards("Minimum confirmed", displayedDate.map(i => i._2).min.toString)
    val maxCasesLabel = new SmallCards("Maximum confirmed", displayedDate.map(i => i._2).max.toString)
    val minDeathsLabel = new SmallCards("Minimum death", displayedDate.map(i => i._3).min.toString)
    val maxDeathsLabel = new SmallCards("Maximum death", displayedDate.map(i => i._3).max.toString)
    val casesSD = new SmallCards("Std deviation confirmed", (Math.round(Math.sqrt(displayedDate.map(i => Math.pow(i._2.toDouble - averageCasesVal, 2.0)).sum/7.0)*100.00)/100.00).toString)
    val deathSD = new SmallCards("Std deviation death", (Math.round(Math.sqrt(displayedDate.map(i => Math.pow(i._3.toDouble - averageDeathsVal, 2.0)).sum/7.0)*100.00)/100.00).toString)

    // link components
    cardsContainer.children = Array(sumCasesContainer, sumDeathContainer)
    calcContainer1.children = Array(averageCasesLabel, averageDeathsLabel)
    calcContainer2.children = Array(maxCasesLabel, maxDeathsLabel)
    calcContainer3.children = Array(minCasesLabel, minDeathsLabel)
    calcContainer4.children = Array(casesSD, deathSD)
    calcContainer.children = Array(calcContainer1, calcContainer2, calcContainer3, calcContainer4)
    container.children = Array(cardsContainer, calcContainer)
    contentContainer.children = Array(titleContainer, container, hideButton)
    this.children = contentContainer

    // style and align components
    cardsContainer.spacing = 10
    container.spacing = 20
    calcContainer.spacing = 5
    calcContainer1.spacing = 5
    calcContainer2.spacing = 5
    calcContainer3.spacing = 5
    calcContainer4.spacing = 5
    contentContainer.spacing = 20

    container.alignment = Pos.Center
    calcContainer.alignment = Pos.Center
    calcContainer1.alignment = Pos.Center
    calcContainer2.alignment = Pos.Center
    calcContainer3.alignment = Pos.Center
    calcContainer4.alignment = Pos.Center
    titleContainer.children = title
    titleContainer.setBackground(new Background(Array(new BackgroundFill(Color.Orange, new CornerRadii(0), Insets(0)))))
    titleContainer.setPadding(Insets(10, 10, 10, 30))
    title.setTextFill(Color.White)
    title.setFont(Font.font("", FontWeight.Bold, 17))
  }
}

// large cards (Sum death - sum cases)
class Cards(title: String, text: String) extends VBox {
  // create shadow effect
  val dropShadow = new DropShadow()
  dropShadow.setRadius(5.0)
  dropShadow.setSpread(0)
  dropShadow.setOffsetX(0.0)
  dropShadow.setOffsetY(1.0)
  dropShadow.setColor(Color.color(0, 0, 0, 0.15))
  // choose red if "death" - choose orange if "cases"
  var color = Color.Red
  if(title != "total death cases") color = Color.Orange

  // style Cards
  this.setBackground(new Background(Array(new BackgroundFill(Color.White, new CornerRadii(5), Insets(0)))))
  this.padding = Insets(15)
  this.alignment = Pos.CenterLeft
  this.setEffect(dropShadow)

  // set titles
  val titleLabel = new Label(title)
  titleLabel.setFont(new Font(12))
  titleLabel.setTextFill(color)

  val textLabel = new Label(text)
  textLabel.setFont(Font.font("", FontWeight.Bold, 20))
  textLabel.setTextFill(color)

  this.children = Array(textLabel, titleLabel)

  // set up hover-exit event handler (for extra effect)
  this.onMouseEntered = (e) => {
    titleLabel.setTextFill(Color.White)
    textLabel.setTextFill(Color.White)
  this.setBackground(new Background(Array(new BackgroundFill(color, new CornerRadii(5), Insets(0)))))
  }
   this.onMouseExited = (e) => {
    titleLabel.setTextFill(color)
    textLabel.setTextFill(color)
    this.setBackground(new Background(Array(new BackgroundFill(Color.White, new CornerRadii(5), Insets(0)))))
  }
}

// small cards - other values
class SmallCards(title: String, text: String) extends VBox {
  // create shadow effect
  val dropShadow = new DropShadow()
  dropShadow.setRadius(5.0)
  dropShadow.setSpread(0)
  dropShadow.setOffsetX(0.0)
  dropShadow.setOffsetY(1.0)
  dropShadow.setColor(Color.color(0, 0, 0, 0.30))
  val clearShadow = new DropShadow()
  clearShadow.setColor(Color.color(0, 0, 0, 0.15))
  // style small cards
  this.padding = Insets(8)
  this.alignment = Pos.Center
  this.setEffect(clearShadow)
  this.setBackground(new Background(Array(new BackgroundFill(Color.White, new CornerRadii(0), Insets(0)))))
  this.setBorder(new Border(new BorderStroke(Color.White, Color.White, Color.White, Color.White,
          BorderStrokeStyle.None, BorderStrokeStyle.None, BorderStrokeStyle.None, BorderStrokeStyle.None,
          CornerRadii.Empty, new BorderWidths(5), Insets.Empty)))

  // set titles
  val titleLabel = new Label(title)
  titleLabel.setFont(new Font(12))
  val textLabel = new Label(text)
  textLabel.setFont(Font.font("", FontWeight.Bold, 18))

  this.children = Array(textLabel, titleLabel)

  // set up hover-exit event handler (for extra effect)
  this.onMouseEntered = (e) => {
    this.setEffect(dropShadow)
  }
  this.onMouseExited = (e) => {
    this.setEffect(clearShadow)
  }
}
