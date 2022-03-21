import scalafx.Includes.observableList2ObservableBuffer
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.canvas.Canvas
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{Border, BorderStroke, BorderStrokeStyle, BorderWidths, ColumnConstraints, CornerRadii, GridPane, HBox, Pane, RowConstraints, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.Font
import scalafx.geometry.{Pos, Side}
import scalafx.stage.{FileChooser, Stage}
import scalafx.collections.ObservableBuffer
import scalafx.stage.Stage
import scalafx.scene.chart._
import scalafx.util.Duration
import scalafx.scene.shape.Circle
import scalafx.scene.chart.PieChart
import scalafx.scene.control.Tooltip
import scalafx.scene.paint.Color

class Diagram(data: Seq[Tuple4[String, Int, Int, Int]], date: String) extends GridPane {
  this.alignment = Pos.Center
  if(data.nonEmpty) {
    println("YES")
    val displayedDate = data.slice(data.map(_._1).indexOf(date), data.map(_._1).indexOf(date)+7)
    val pieChartContainer = new PieChartModel(displayedDate)
    val scatterPlotContainer = new ScatterPlotModel(displayedDate)
    val columnPlotContainer = new ColumnPlotModel(displayedDate)
    this.add(pieChartContainer, 0, 0)
    this.add(scatterPlotContainer, 1, 0)
    this.add(columnPlotContainer, 2, 0)

    val column0 = new ColumnConstraints
    val column1 = new ColumnConstraints
    val column2 = new ColumnConstraints

    column0.percentWidth = 33.33
    column1.percentWidth = 33.33
    column2.percentWidth = 33.33

    this.columnConstraints = Array[ColumnConstraints](column0, column1, column2)
  }
}

class PieChartModel(data: Seq[Tuple4[String, Int, Int, Int]]) extends VBox {
// this.setBorder(new Border(new BorderStroke(Color.Black,
//            BorderStrokeStyle.Solid, new CornerRadii(5.0), BorderWidths.Default)))
  val sumCases = data.map(_._2).sum
  var sumDeath = data.map(_._3).sum
  var note = new Label("")
  val pieChartData = ObservableBuffer(
      PieChart.Data("Death cases", sumDeath),
      PieChart.Data("Cofirmed cases", sumCases)
    )
  var total: Double = _
  val pie: PieChart = new PieChart()
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
  this.children = pie
  pie.getData.foreach(total += _.getPieValue)
  pie.getData.foreach(
    d => {
      val sliceNode = d.getNode
      var pieValue = d.getPieValue
      val percent = (pieValue/total)*100.0
      val msg = "%s: %.2f (%.2f%%)".format(d.getName, pieValue, percent)
      val tt = new Tooltip {
        text = msg
        showDelay = Duration(0)
      }
      Tooltip.install(sliceNode, tt)
  })
}

class ScatterPlotModel(data: Seq[Tuple4[String, Int, Int, Int]]) extends VBox {
  val sumCasesArr = data.map(_._2)
  val sumDeathsArr = data.map(_._3)
  val datesArr = ObservableBuffer() ++ data.map(_._1)

  val xAxis = new CategoryAxis()
  xAxis.setCategories(datesArr)
  xAxis.setLabel("Type of case")

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
          val barNode = i.getNode
          var barValue:Double = i.getYValue.toString.toDouble
          val msg = "%s: %.2f".format(d.getName, barValue)
          val tt = new Tooltip {
            text = msg
            showDelay = Duration(0)
          }
          Tooltip.install(barNode, tt)
        }
      )
    })
}

class ColumnPlotModel(data: Seq[Tuple4[String, Int, Int, Int]]) extends VBox {

}