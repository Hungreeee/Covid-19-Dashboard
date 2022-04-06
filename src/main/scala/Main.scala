import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.{ColumnConstraints, GridPane, HBox, RowConstraints, VBox}
import scalafx.geometry.{Orientation, Pos}
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

  // initiate components
  lazy val diagram = new VBox()
  diagram.children = new Diagram(Seq[Tuple4[String, Int, Int, Int]](), "06/09/2021")
  lazy val table = new VBox()
  table.children = new DataTable(Seq[Tuple4[String, Int, Int, Int]](), "06/09/2021")
  val uploader = new VBox()
  uploader.children = new FileHandler
  lazy val saver = new HBox()
  saver.children = new FileSaver(Seq[Tuple4[String, Int, Int, Int]](), "06/09/2021")

  diagram.visible = false
  table.visible = false

  // add splitpane to components
  diagram.alignment = Pos.Center
  table.alignment = Pos.Center
  uploader.alignment = Pos.Center
  saver.alignment = Pos.Center

  val diagramContainer = new SplitPane()
  diagramContainer.getItems.addAll(uploader, table)
  diagramContainer.orientation = Orientation.Horizontal

  val container = new SplitPane()
  container.getItems.addAll(diagram, diagramContainer)
  container.orientation = Orientation.Vertical

  root.add(container, 0, 0)

  // align components
  val column0 = new ColumnConstraints
  val row0 = new RowConstraints

  column0.percentWidth = 100
  row0.percentHeight = 100

  root.columnConstraints = Array[ColumnConstraints](column0)
  root.rowConstraints = Array[RowConstraints](row0)
}
