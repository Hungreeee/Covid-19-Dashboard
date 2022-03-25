import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.ColumnConstraints
import scalafx.scene.layout.RowConstraints
import scalafx.geometry.Orientation
import scalafx.scene.control.SplitPane

object Main extends JFXApp {
  val root = new GridPane
  val scene = new Scene(root)
  stage = new JFXApp.PrimaryStage {
    title.value = "Data Dashboard"
  }
  stage.scene = scene
  stage.setResizable(false)
  stage.setMaximized(true)

  lazy val diagram = new Diagram(Seq[Tuple4[String, Int, Int, Int]](), "06/09/2021")
  lazy val table = new DataTable(Seq[Tuple4[String, Int, Int, Int]](), "06/09/2021")
  val uploader = new FileHandler

  diagram.visible = false
  table.visible = false

  val diagramContainer = new SplitPane()
  diagramContainer.getItems.addAll(uploader, table)
  diagramContainer.orientation = Orientation.Horizontal

  val container = new SplitPane()
  container.getItems.addAll(diagram, diagramContainer)
  container.orientation = Orientation.Vertical

  root.add(container, 0, 0)

  val column0 = new ColumnConstraints
  val row0 = new RowConstraints

  column0.percentWidth = 100
  row0.percentHeight = 100

  root.columnConstraints = Array[ColumnConstraints](column0)
  root.rowConstraints = Array[RowConstraints](row0)
}