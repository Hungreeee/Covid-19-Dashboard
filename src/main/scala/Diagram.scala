import scalafx.Includes.observableList2ObservableBuffer
import scalafx.collections.ObservableBuffer
import scalafx.scene.control._
import scalafx.scene.layout.{Background, BackgroundFill, CornerRadii, HBox, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.{Font, FontWeight}
import scalafx.geometry.{Insets, Pos}
import scalafx.stage.Screen
import scalafx.scene.chart._
import scalafx.util.Duration
import scalafx.scene.control.Tooltip
import scalafx.scene.effect.DropShadow
import scalafx.scene.paint.Color
import scalafx.Includes._
import scalafx.scene.Node

class Diagram(data: Seq[Tuple4[String, Int, Int, Int]], date: String) extends VBox {
  // if data is valid
  if(data.nonEmpty) {
    // initiate components
    val hideButton = new Button("Hide")
    val showButton = new Button("Show")
    val contentContainer = new HBox()
    val container = new VBox()
    val displayedDate = data.slice(data.map(_._1).indexOf(date), data.map(_._1).indexOf(date)+7)
    val title = new Label("Diagrams displaying data from " + date + " to " + data.map(_._1).apply(data.map(_._1).indexOf(date) + 6)+ (if(date == "06/09/2021") " (Default)" else ""))
    val pieChartContainer = new PieChartModel(displayedDate)
    val scatterPlotContainer = new ScatterPlotModel(displayedDate)
    val columnPlotContainer = new ColumnPlotModel(displayedDate)
    val graphContainer = new HBox()
    val controlTitle = new Label("Diagram Controller")
    val controlContainer = new ControlContainer()
    val slot1Container = new SlotCard("Slot 1")
    val slot2Container = new SlotCard("Slot 2")
    val slot3Container = new SlotCard("Slot 3")
    val titleContainer = new HBox()

    // style, link and align components
    titleContainer.setBackground(new Background(Array(new BackgroundFill(Color.Orange, new CornerRadii(0), Insets(0)))))
    titleContainer.setPadding(Insets(10, 10, 10, 30))
    title.setTextFill(Color.White)
    title.setFont(Font.font("", FontWeight.Bold, 18))
    controlTitle.setFont(Font.font("", FontWeight.Bold, 16))

    graphContainer.alignment = Pos.Center
    container.alignment = Pos.Center
    this.alignment = Pos.Center
    contentContainer.alignment = Pos.Center
    controlContainer.alignment = Pos.TopCenter
    container.spacing = 20
    contentContainer.spacing = 20
    controlContainer.spacing = 20
    graphContainer.spacing = 20
    graphContainer.minWidth = Screen.primary.visualBounds.width / 1.2
    graphContainer.maxWidth  = Screen.primary.visualBounds.width / 1.2
    graphContainer.prefWidth = Screen.primary.visualBounds.width / 1.2

    titleContainer.children = title
    controlContainer.children = Array(controlTitle, slot1Container, slot2Container, slot3Container)
    graphContainer.children = Array(pieChartContainer, scatterPlotContainer, columnPlotContainer)
    contentContainer.children = Array(controlContainer, graphContainer)
    container.children = Array(titleContainer, contentContainer, hideButton)
    this.children = container

    // Diagram controller--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Initiate components
    val showPie1 = new CustomButton("Show pie chart")
    val showPie2 = new CustomButton("Show pie chart")
    val showPie3 = new CustomButton("Show pie chart")
    val showBar1 = new CustomButton("Show bar chart")
    val showBar2 = new CustomButton("Show bar chart")
    val showBar3 = new CustomButton("Show bar chart")
    val showScatter1 = new CustomButton("Show scatter plot")
    val showScatter2 = new CustomButton("Show scatter plot")
    val showScatter3 = new CustomButton("Show scatter plot")
    val hide1 = new CustomHideButton
    val hide2 = new CustomHideButton
    val hide3 = new CustomHideButton
    val butContainer1R1 = new HBox()
    val butContainer1R2 = new HBox()
    val butContainer2R1 = new HBox()
    val butContainer2R2 = new HBox()
    val butContainer3R1 = new HBox()
    val butContainer3R2 = new HBox()
    val slotTitle1 = new Label("Slot 1")
    val slotTitle2 = new Label("Slot 2")
    val slotTitle3 = new Label("Slot 3")

    // style, link and create event hanlder for buttons in the Controller
    slotTitle1.setFont(Font.font("", FontWeight.Bold, 12))
    slotTitle2.setFont(Font.font("", FontWeight.Bold, 12))
    slotTitle3.setFont(Font.font("", FontWeight.Bold, 12))

    showPie1.disable = true
    showScatter2.disable = true
    showBar3.disable = true

    butContainer1R1.children = Array(showPie1, showBar1)
    butContainer1R2.children = Array(showScatter1, hide1)
    butContainer2R1.children = Array(showPie2, showBar2)
    butContainer2R2.children = Array(showScatter2, hide2)
    butContainer3R1.children = Array(showPie3, showBar3)
    butContainer3R2.children = Array(showScatter3, hide3)

    slot1Container.children = Array(slotTitle1, butContainer1R1, butContainer1R2)
    slot2Container.children = Array(slotTitle2, butContainer2R1, butContainer2R2)
    slot3Container.children = Array(slotTitle3, butContainer3R1, butContainer3R2)
    showPie1.onAction = (e) => {
      graphContainer.children(0) = new PieChartModel(displayedDate)
      showPie1.disable = true
      showBar1.disable = false
      showScatter1.disable = false
      hide1.disable = false
    }
    showPie2.onAction = (e) => {
      graphContainer.children(1) = new PieChartModel(displayedDate)
      showPie2.disable = true
      showBar2.disable = false
      showScatter2.disable = false
      hide2.disable = false
    }
    showPie3.onAction = (e) => {
      graphContainer.children(2) = new PieChartModel(displayedDate)
      showPie3.disable = true
      showBar3.disable = false
      showScatter3.disable = false
      hide3.disable = false
    }
    showBar1.onAction = (e) => {
      graphContainer.children(0) = new ColumnPlotModel(displayedDate)
      showPie1.disable = false
      showBar1.disable = true
      showScatter1.disable = false
      hide1.disable = false
    }
    showBar2.onAction = (e) => {
      graphContainer.children(1) = new ColumnPlotModel(displayedDate)
      showPie2.disable = false
      showBar2.disable = true
      showScatter2.disable = false
      hide2.disable = false
    }
    showBar3.onAction = (e) => {
      graphContainer.children(2) = new ColumnPlotModel(displayedDate)
      showPie3.disable = false
      showBar3.disable = true
      showScatter3.disable = false
      hide3.disable = false
    }
    showScatter1.onAction = (e) => {
      graphContainer.children(0) = new ScatterPlotModel(displayedDate)
      showPie1.disable = false
      showBar1.disable = false
      showScatter1.disable = true
      hide1.disable = false
    }
    showScatter2.onAction = (e) => {
      graphContainer.children(1) = new ScatterPlotModel(displayedDate)
      showPie2.disable = false
      showBar2.disable = false
      showScatter2.disable = true
      hide2.disable = false
    }
    showScatter3.onAction = (e) => {
      graphContainer.children(2) = new ScatterPlotModel(displayedDate)
      showPie3.disable = false
      showBar3.disable = false
      showScatter3.disable = true
      hide3.disable = false
    }
    hide1.onAction = (e) => {
      graphContainer.children(0) = new HBox()
      showPie1.disable = false
      showBar1.disable = false
      showScatter1.disable = false
      hide1.disable = true
    }
    hide2.onAction = (e) => {
      graphContainer.children(1) = new HBox()
      showPie2.disable = false
      showBar2.disable = false
      showScatter2.disable = false
      hide2.disable = true
    }
    hide3.onAction = (e) => {
      graphContainer.children(2) = new HBox()
      showPie3.disable = false
      showBar3.disable = false
      showScatter3.disable = false
      hide3.disable = true
    }

    // align and style components
    slot1Container.alignment = Pos.Center
    butContainer1R1.alignment = Pos.Center
    butContainer1R2.alignment = Pos.Center
    slot1Container.spacing = 5
    butContainer1R1.spacing = 5
    butContainer1R2.spacing = 5
    slot2Container.alignment = Pos.Center
    butContainer2R1.alignment = Pos.Center
    butContainer2R2.alignment = Pos.Center
    slot2Container.spacing = 5
    butContainer2R1.spacing = 5
    butContainer2R2.spacing = 5
    slot3Container.alignment = Pos.Center
    butContainer3R1.alignment = Pos.Center
    butContainer3R2.alignment = Pos.Center
    slot3Container.spacing = 5
    butContainer3R1.spacing = 5
    butContainer3R2.spacing = 5
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    // create shadow effect
    val dropShadow = new DropShadow()
    dropShadow.setRadius(5.0)
    dropShadow.setSpread(0)
    dropShadow.setOffsetX(0.0)
    dropShadow.setOffsetY(1.0)
    dropShadow.setColor(Color.color(0, 0, 0, 0.20))
    val clearShadow = new DropShadow()
    clearShadow.setColor(Color.color(0, 0, 0, 0))

    // style and set event handler for hide-show buttons
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
      this.children = container
    }

  }
}

class PieChartModel(data: Seq[Tuple4[String, Int, Int, Int]]) extends VBox {
  // Create pie chart model
  val sumCases = data.map(_._2).sum
  var sumDeath = data.map(_._3).sum
  var note = new Label("")
  val pieChartData = ObservableBuffer(
      PieChart.Data("Death cases", sumDeath),
      PieChart.Data("Cofirmed cases", sumCases)
    )
  var total: Double = _
  val pie: PieChart = new PieChart()
  this.children = pie

  pie.setTitle("Pie Chart")
  pie.setData(pieChartData)
  pie.setPrefHeight(400)
  pie.setMinHeight(400)
  pie.setMaxHeight(400)

  pie.setPrefWidth(400)
  pie.setMinWidth(400)
  pie.setMaxWidth(400)

  pie.setPrefSize(400, 400)
  pie.setMinSize(400, 400)
  pie.setMaxSize(400, 400)
  pie.setLabelsVisible(false)
  pie.getData.foreach(total += _.getPieValue)

  pie.getData.foreach(
    d => {
      val sliceNode:Node = d.getNode
      var pieValue = d.getPieValue
      val percent = (pieValue/total)*100.0
      val msg = "%s: %.2f (%.2f%%)".format(d.getName, pieValue, percent)
      // Show tooltip when hovered
      val tt = new Tooltip {
        text = msg
        showDelay = Duration(0)
      }
      Tooltip.install(sliceNode, tt)
  })
}

class ColumnPlotModel(data: Seq[Tuple4[String, Int, Int, Int]]) extends VBox {
  // Create column chart model
  val sumCasesArr = data.map(_._2)
  val sumDeathsArr = data.map(_._3)
  val datesArr = ObservableBuffer() ++ data.map(_._1).map(i => i.substring(0, 6) + i.substring(8, 10))

  val xAxis = new CategoryAxis()
  xAxis.setCategories(datesArr)

  val yAxis = new NumberAxis()
  yAxis.setLabel("Number of cases")

  val series1 = new XYChart.Series[String, Number]()
  series1.setName("Confirmed cases")
  series1.getData.add(XYChart.Data(datesArr.head, sumCasesArr.head))
  series1.getData.add(XYChart.Data(datesArr(1), sumCasesArr(1)))
  series1.getData.add(XYChart.Data(datesArr(2), sumCasesArr(2)))
  series1.getData.add(XYChart.Data(datesArr(3), sumCasesArr(3)))
  series1.getData.add(XYChart.Data(datesArr(4), sumCasesArr(4)))
  series1.getData.add(XYChart.Data(datesArr(5), sumCasesArr(5)))
  series1.getData.add(XYChart.Data(datesArr(6), sumCasesArr(6)))

  val series2 = new XYChart.Series[String, Number]()
  series2.setName("Death cases")
  series2.getData.add(XYChart.Data(datesArr.head, sumDeathsArr.head))
  series2.getData.add(XYChart.Data(datesArr(1), sumDeathsArr(1)))
  series2.getData.add(XYChart.Data(datesArr(2), sumDeathsArr(2)))
  series2.getData.add(XYChart.Data(datesArr(3), sumDeathsArr(3)))
  series2.getData.add(XYChart.Data(datesArr(4), sumDeathsArr(4)))
  series2.getData.add(XYChart.Data(datesArr(5), sumDeathsArr(5)))
  series2.getData.add(XYChart.Data(datesArr(6), sumDeathsArr(6)))

  val barChart = new BarChart(xAxis, yAxis)
  this.children = barChart
  barChart.getData.addAll(series2, series1)
  barChart.setTitle("Bar Chart")
  barChart.getData.foreach(
    d => {
      d.getData.foreach(
        i => {
          val barNode:Node = i.getNode
          var barValue:Double = i.getYValue.toString.toDouble
          val msg = "%s: %.2f".format(d.getName, barValue)
          // Tooltip installing
          val tt = new Tooltip {
            text = msg
            showDelay = Duration(0)
          }
          Tooltip.install(barNode, tt)
        }
      )
    })
  barChart.setPrefHeight(400)
  barChart.setMinHeight(400)
  barChart.setMaxHeight(400)

  barChart.setPrefWidth(400)
  barChart.setMinWidth(400)
  barChart.setMaxWidth(400)

  barChart.setPrefSize(400, 400)
  barChart.setMinSize(400, 400)
  barChart.setMaxSize(400, 400)
}

class ScatterPlotModel(data: Seq[Tuple4[String, Int, Int, Int]]) extends VBox {
  // Create scatter chart model
  val sumCasesArr = data.map(_._2)
  val sumDeathsArr = data.map(_._3)
  val datesArr = ObservableBuffer() ++ data.map(_._1).map(i => i.substring(0, 6) + i.substring(8, 10))

  val xAxis = new CategoryAxis()
  xAxis.setCategories(datesArr)

  val yAxis = new NumberAxis()
  yAxis.setLabel("Number of cases")

  val series1 = new XYChart.Series[String, Number]()
  series1.setName("Confirmed cases")
  series1.getData.add(XYChart.Data(datesArr.head, sumCasesArr.head))
  series1.getData.add(XYChart.Data(datesArr(1), sumCasesArr(1)))
  series1.getData.add(XYChart.Data(datesArr(2), sumCasesArr(2)))
  series1.getData.add(XYChart.Data(datesArr(3), sumCasesArr(3)))
  series1.getData.add(XYChart.Data(datesArr(4), sumCasesArr(4)))
  series1.getData.add(XYChart.Data(datesArr(5), sumCasesArr(5)))
  series1.getData.add(XYChart.Data(datesArr(6), sumCasesArr(6)))
  val series2 = new XYChart.Series[String, Number]()
  series2.setName("Death cases")
  series2.getData.add(XYChart.Data(datesArr.head, sumDeathsArr.head))
  series2.getData.add(XYChart.Data(datesArr(1), sumDeathsArr(1)))
  series2.getData.add(XYChart.Data(datesArr(2), sumDeathsArr(2)))
  series2.getData.add(XYChart.Data(datesArr(3), sumDeathsArr(3)))
  series2.getData.add(XYChart.Data(datesArr(4), sumDeathsArr(4)))
  series2.getData.add(XYChart.Data(datesArr(5), sumDeathsArr(5)))
  series2.getData.add(XYChart.Data(datesArr(6), sumDeathsArr(6)))

  val scatterChart = new ScatterChart(xAxis, yAxis)
  this.children = scatterChart
  scatterChart.getData.addAll(series2, series1)
  scatterChart.setTitle("Scatter Chart")
  scatterChart.getData.foreach(
    d => {
      d.getData.foreach(
        i => {
          val barNode:Node = i.getNode
          var barValue:Double = i.getYValue.toString.toDouble
          val msg = "%s: %.2f".format(d.getName, barValue)
          // Tooltip installing
          val tt = new Tooltip {
            text = msg
            showDelay = Duration(0)
          }
          Tooltip.install(barNode, tt)
        }
      )
    })
  scatterChart.setPrefHeight(400)
  scatterChart.setMinHeight(400)
  scatterChart.setMaxHeight(400)

  scatterChart.setPrefWidth(400)
  scatterChart.setMinWidth(400)
  scatterChart.setMaxWidth(400)

  scatterChart.setPrefSize(400, 400)
  scatterChart.setMinSize(400, 400)
  scatterChart.setMaxSize(400, 400)
}

class ControlContainer extends VBox {
  // style Diagram Controller
  val dropShadow = new DropShadow()
  dropShadow.setRadius(5.0)
  dropShadow.setSpread(0)
  dropShadow.setOffsetX(0.0)
  dropShadow.setOffsetY(1.0)
  dropShadow.setColor(Color.color(0, 0, 0, 0.30))
  this.padding = Insets(10, 10, 100, 10)
  this.alignment = Pos.TopCenter
  this.spacing = 5
  this.setBackground(new Background(Array(new BackgroundFill(Color.White, new CornerRadii(5), Insets(0)))))
  this.setEffect(dropShadow)
}

class SlotCard(title: String) extends VBox {
  // style slot container for Diagram Controller
  val dropShadow = new DropShadow()
  dropShadow.setRadius(5.0)
  dropShadow.setSpread(0)
  dropShadow.setOffsetX(0.0)
  dropShadow.setOffsetY(1.0)
  dropShadow.setColor(Color.color(0, 0, 0, 0.30))

  this.alignment = Pos.Center
  this.setBackground(new Background(Array(new BackgroundFill(Color.White, new CornerRadii(0), Insets(0)))))
  this.spacing = 5
}

class CustomButton(title: String) extends Button {
  // create a custom button for Diagram Controller
  val dropShadow = new DropShadow()
  dropShadow.setRadius(5.0)
  dropShadow.setSpread(0)
  dropShadow.setOffsetX(0.0)
  dropShadow.setOffsetY(1.0)
  dropShadow.setColor(Color.color(0, 0, 0, 0.30))
  val clearShadow = new DropShadow()
  clearShadow.setColor(Color.color(0, 0, 0, 0))

  this.setText(title)
  this.setBackground(new Background(Array(new BackgroundFill(Color.Orange, new CornerRadii(0), Insets(0)))))
  this.setTextFill(Color.White)
  this.setFont(Font.font("", FontWeight.Bold, 10))
  this.onMouseEntered = (e) => {
    this.setEffect(dropShadow)
    this.setBackground(new Background(Array(new BackgroundFill(Color.White, new CornerRadii(0), Insets(0)))))
    this.setTextFill(Color.Black)
  }
  this.onMouseExited = (e) => {
    this.setEffect(clearShadow)
    this.setBackground(new Background(Array(new BackgroundFill(Color.Orange, new CornerRadii(0), Insets(0)))))
    this.setTextFill(Color.White)
  }
}

class CustomHideButton extends Button {
  // create a custom hide diagram button for Diagram Controller
  this.setText("Hide chart")
  val dropShadow = new DropShadow()
  dropShadow.setRadius(5.0)
  dropShadow.setSpread(0)
  dropShadow.setOffsetX(0.0)
  dropShadow.setOffsetY(1.0)
  dropShadow.setColor(Color.color(0, 0, 0, 0.30))
  val clearShadow = new DropShadow()
  clearShadow.setColor(Color.color(0, 0, 0, 0.15))

  this.setBackground(new Background(Array(new BackgroundFill(Color.White, new CornerRadii(0), Insets(0)))))
  this.setEffect(clearShadow)
  this.setTextFill(Color.Black)
  this.setFont(Font.font("", FontWeight.Bold, 10))

  this.onMouseEntered = (e) => {
    this.setEffect(dropShadow)
    this.setTextFill(Color.Orange)
  }
  this.onMouseExited = (e) => {
    this.setEffect(clearShadow)
    this.setTextFill(Color.Black)
  }
}
