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
import scalafx.scene.layout.{HBox, Pane, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.Font
import scalafx.stage.{FileChooser, Stage}

class DataTable(data: Seq[Tuple4[String, Int, Int, Int]]) extends VBox{
  if(data.nonEmpty) {
    val population = data.size
    val maxValueCases = new Label("Max case: " + data.maxBy(_._2)._2.toString)
    val maxValueDeaths = new Label("Max death: " + data.maxBy(_._3)._3.toString)
    var sumCases = 0
    var sumDeaths = 0
    data.foreach(i => sumCases += i._2)
    data.foreach(i => sumDeaths += i._3)
    val sumCasesLabel = new Label("Sum case: " + sumCases.toString)
    val sumDeathsLabel = new Label("Sum death: " + sumCases.toString)
    var sdCases = 0.0
    var sdDeaths = 0.0
    data.foreach(i => sdCases += Math.pow((i._2-(sumCases*1.0/population)),2))
    data.foreach(i => sdDeaths += Math.pow((i._2-(sumDeaths*1.0/population)),2))
    sdCases = Math.sqrt(sdCases/population*1.0)
    sdDeaths = Math.sqrt(sdDeaths/population*1.0)
    val avCase = new Label("Average of case: " + (sumCases*1.0/population).toString)
    val avDeath = new Label("Average of deaths: " + (sumDeaths*1.0/population).toString)
    val sdCasesLabel = new Label("Standard deviation of case: " + sdCases.toString)
    val sdDeathsLabel = new Label("Standard deviation of death: " + sdDeaths.toString)
    this.children = Iterable[Node](maxValueCases, maxValueDeaths, sumCasesLabel, sumDeathsLabel, avCase, avDeath, sdCasesLabel, sdDeathsLabel)
  }
}
