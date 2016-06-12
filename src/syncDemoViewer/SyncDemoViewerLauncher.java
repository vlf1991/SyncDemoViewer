package syncDemoViewer;

public class SyncDemoViewerLauncher {

	public static void main(String[] args) {

		SyncDemoViewerProperties properties = SyncDemoViewerProperties.getProperties();
		Controller controller = new Controller(properties);
		GUI viewer = new GUI();
		viewer.setController(controller);
		controller.setViewer(viewer);

	}

}
