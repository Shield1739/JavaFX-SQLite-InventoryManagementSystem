module mp.utp.xyz {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;

	opens mp.utp.xyz to javafx.fxml;
	opens mp.utp.xyz.controllers.init to javafx.fxml;
	opens mp.utp.xyz.controllers.main to javafx.fxml;

	opens mp.utp.xyz.data to javafx.base;

	exports mp.utp.xyz;
	exports mp.utp.xyz.controllers.init;
	exports mp.utp.xyz.controllers.main;
}