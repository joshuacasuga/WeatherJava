package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.json.*;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {

  public final static String map = "API KEY";

  public static String city = "";

  public static boolean toggle = true;
  public static String icon = "";
  public static int humidity = 0;
  public static int wind = 0;
  public static String description = "empty";
  public static int temp = 0;
  public static int tempMax = 0;
  public static int tempMin = 0;
  public static int feels = 0;
  public static int sunrise = 0;
  public static int sunset = 0;
  public static double pressure = 0;
  public static String timezone = "";
  public static String country = "";
  public static int morning = 0;
  public static String morningIcon = "";
  public static int afternoon = 0;
  public static String afternoonIcon = "";
  public static int evening = 0;
  public static String eveningIcon = "";
  public static int night = 0;
  public static String nightIcon = "";
  public static ArrayList<String> forecastDates = new ArrayList<>();
  public static ArrayList<Integer> forecastTemp = new ArrayList<>();
  public static ArrayList<String> forecastDescr = new ArrayList<>();
  public static ArrayList<Integer> forecastHigh = new ArrayList<>();
  public static ArrayList<Integer> forecastLow = new ArrayList<>();
  public static ArrayList<HBox> previous = new ArrayList<>();
  public static ArrayList<Integer> forecastToday = new ArrayList<>();
  public static ArrayList<String> forecastIcon = new ArrayList<>();
  public static String title;

  public static void main(String [] args) throws URISyntaxException, IOException {
    launch(args);
  }

  //@Override
  public void start(Stage primaryStage) throws Exception {
    uiStart(primaryStage);
  }

// HOME
  
  private static void uiStart(Stage primaryStage) {

    // TODO Auto-generated method stub

    DropShadow drop = new DropShadow();
    drop.setOffsetX(0);
    drop.setOffsetY(0);
    drop.setColor(Color.web("#C9C9C9"));

    // logo
    ImageView view = new ImageView();
    Image logos = new Image("file:./res/logo2.png");
    view.setImage(logos);
    view.setPreserveRatio(true);
    view.setSmooth(true);
    view.setFitWidth(57);
    view.setTranslateX(320);
    view.setTranslateY(11);

    view.setOnMouseClicked(event -> {
      try {
        restartApp(primaryStage);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    });

    Text ibm = new Text("An IBM Business");
    ibm.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    ibm.setFill(Color.WHITE);
    ibm.setTranslateX(401);
    ibm.setTranslateY(45);

    TextField search = new TextField();
    search.setPromptText("Search City or Zip Code");
    search.setAlignment(Pos.CENTER);
    search.setPrefSize(380, 30);
    search.setTranslateX(705);
    search.setTranslateY(25);
    search.setStyle(
        "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-font-size: 14;" +
            "-fx-background-color: #337A9E;" +
        "-fx-text-fill: white;");

    search.setOnKeyPressed(e -> {
      if(e.getCode() == KeyCode.ENTER) {
        city = search.getText();
        try {
          APICall(city);

          secondStage();
          PauseTransition delay = new PauseTransition(Duration.millis(500));
          delay.setOnFinished(event -> {
            primaryStage.close();
          });
          delay.play();

        }  catch (URISyntaxException | IOException e1) {
          // TODO Auto-generated catch block
          // e1.printStackTrace();
        }
      }
    });

    // search icon
    ImageView searcher = new ImageView();
    Image searched = new Image("file:./res/search.png");
    searcher.setImage(searched);
    searcher.setPreserveRatio(true);
    searcher.setSmooth(true);
    searcher.setFitWidth(20);
    searcher.setTranslateX(1052);
    searcher.setTranslateY(30);

    // header
    Rectangle header = new Rectangle();
    header.setWidth(1920);
    header.setHeight(80);
    header.setFill(Color.web("#005986"));

    // F / C    	
    ToggleButton fc = new ToggleButton("°C");
    fc.setPrefWidth(45);
    if(!toggle) {
      fc.setStyle(
          "-fx-base: #EF9E1C;" +
              "-fx-text-fill: #FFFFFF;" +
              "-fx-background-color: #82D1DB;" +
              "-fx-background-insets: 0, 1;" +
              "-fx-background-radius: 5em;" +
              "-fx-padding: 5px;" +
              "-fx-font-size: 14px;" +
              "-fx-content-display: center;"
          );
    } else {
      fc.setStyle(
          "-fx-background-color: #82D1DB;" +
              "-fx-text-fill: #FFFFFF;" +
              "-fx-background-insets: 0, 1;" +
              "-fx-background-radius: 5em;" +
              "-fx-padding: 5px;" +
              "-fx-font-size: 14px;" +
              "-fx-content-display: center;"
          );
    }
    fc.setOnAction(e -> {
      if(!(fc.isSelected())) {
        fc.setText("°F");
        toggle = false;
        fc.setStyle(
            "-fx-background-color: #EF9E1C;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-insets: 0, 1;" +
                "-fx-background-radius: 5em;" +
                "-fx-padding: 5px;" +
                "-fx-font-size: 14px;" +
                "-fx-content-display: center;"
            );
      }
      else {
        fc.setText("°C");
        toggle = true;
        fc.setStyle(
            "-fx-background-color: #82D1DB;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-insets: 0, 1;" +
                "-fx-background-radius: 5em;" +
                "-fx-padding: 5px;" +
                "-fx-font-size: 14px;" +
                "-fx-content-display: center;"
            );
      }
    });
    fc.setTranslateX(1325);
    fc.setTranslateY(25);

    // premium
    Button prem = new Button("GO PREMIUM");
    prem.setPrefSize(80, 30);
    prem.setStyle("-fx-background-color: #FFFFFF;" +
        "-fx-text-fill: #000000;" + 
        "-fx-background-insets: 0, 1;" +
        "-fx-background-radius: 5em;" +
        "-fx-padding: 5px;" +
        "-fx-font-size: 10px");
    prem.setTranslateX(1380);
    prem.setTranslateY(25);
    prem.setOnAction(event -> {
      premiumStage();
      primaryStage.close();
    });

    // remembering previous searches
    HBox mem = new HBox();
    mem.setPrefWidth(1920);
    mem.setPrefHeight(40);
    mem.setTranslateY(80);
    mem.setStyle("-fx-background-color: #337A9E;");

    HBox temporaryArray = new HBox();
    Label temporary = new Label();
    if(previous.size() == 0)
      temporary.setText("");
    else
      temporary = new Label("   " + temp + "\u00B0 " + city);

    temporary.setStyle("-fx-font-size: 18;");
    temporary.setTextFill(Color.WHITE);
    ImageView tempIcon = new ImageView(new Image("https://openweathermap.org/img/w/" + icon + ".png"));
    tempIcon.setPreserveRatio(true);
    tempIcon.setSmooth(true);
    tempIcon.setFitWidth(25);
    temporaryArray.getChildren().add(tempIcon);
    temporaryArray.getChildren().add(temporary);
    //previous.add(temporaryArray);

    //if(previous.size() > 4 || temporary.getText() == "")
    //  previous.remove(0);


    mem.setAlignment(Pos.CENTER_LEFT);
    mem.setSpacing(40);
    for(int i = 0; i < previous.size(); i++) {
      HBox hboxTemp = previous.get(i);
      hboxTemp.setMinWidth(300);
      hboxTemp.setAlignment(Pos.CENTER);
      mem.getChildren().addAll(hboxTemp);

      if(i < previous.size() - 1) {
        Separator separator = new Separator();
        separator.setOrientation(javafx.geometry.Orientation.VERTICAL);
        mem.getChildren().addAll(separator);
      }
    }

    // login
    ImageView login = new ImageView();
    Image logins = new Image("file:./res/login.png");
    login.setImage(logins);
    login.setPreserveRatio(true);
    login.setSmooth(true);
    login.setFitWidth(30);
    login.setTranslateX(1480);
    login.setTranslateY(25);

    // hamburger icon
    Line line1 = new Line(0, 0, 30, 0);
    Line line2 = new Line(0, 10, 30, 10);
    Line line3 = new Line(0, 20, 30, 20);
    Pane ham = new Pane(line1, line2, line3);
    for (Line line : new Line[]{line1, line2, line3}) {
      line.setStrokeWidth(3);
      line.setStroke(Color.WHITE);
    }
    ham.setTranslateX(1540);
    ham.setTranslateY(30);

    // keep this part since this basically holds the entire top portion of the site
    Pane top = new Pane();
    top.getChildren().addAll(header, view, ibm, fc, search, searcher, mem, prem, login);


    // --------------------------------------------------------

    // home page
    Text weatherToday = new Text("Weather Today Across the Country");
    weatherToday.setStyle("-fx-font-size: 20;" +
        "-fx-font-weight: bold;");
    weatherToday.setTranslateX(530);
    weatherToday.setTranslateY(177);

    LocalDate dated = LocalDate.now();
    DateTimeFormatter form = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    String date = dated.format(form);
    date = date.replace(date.substring(0, 3), date.substring(0, 3).toUpperCase());

    DropShadow shadow = new DropShadow();
    shadow.setOffsetX(0);
    shadow.setOffsetY(0);
    shadow.setColor(Color.web("#C9C9C9"));

    Rectangle body = new Rectangle(900, 565);
    body.setFill(Color.WHITE);
    body.setTranslateX(510);
    body.setTranslateY(143);
    body.setArcWidth(20);
    body.setArcHeight(20);
    body.setEffect(shadow);

    ImageView weatherMap = new ImageView();
    Image img = new Image("file:./res/weathermap.png");
    weatherMap.setImage(img);
    weatherMap.setSmooth(true);
    weatherMap.setFitWidth(803);
    weatherMap.setFitHeight(396);
    weatherMap.setTranslateX(558);
    weatherMap.setTranslateY(237);

    Text todayscast = new Text("TODAY'S FORECAST");
    todayscast.setStyle("-fx-font-size: 14;" +
        "-fx-font-weight: bold;");
    todayscast.setTranslateX(530);
    todayscast.setTranslateY(211);
    Text today = new Text(date);
    today.setStyle("-fx-font-size: 14;" +
        "-fx-font-weight: bold;");
    today.setTranslateX(1300);
    today.setTranslateY(211);

    Button readmore = new Button("Read More");
    readmore.setPrefSize(96, 32);
    readmore.setTranslateX(533);
    readmore.setTranslateY(653);
    readmore.setStyle(
        "-fx-text-fill: #FFFFFF;" +
            "-fx-background-color: #1B4DE4;" +
            "-fx-background-insets: 0, 1;" +
            "-fx-background-radius: 5em;" +
            "-fx-padding: 5px;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
        "-fx-content-display: center;" );

    readmore.setOnMouseClicked(event -> {
      setTitle(weatherToday.getText());
      try {
        newsStage();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      primaryStage.close();
    });

    login.setOnMouseClicked(event -> {
      loginStage();
    });


    // ----------------------------------------------------------------

    // news time

    Rectangle news = new Rectangle(900, 1057);
    news.setFill(Color.WHITE);
    news.setTranslateX(510);
    news.setTranslateY(735);
    news.setArcWidth(20);
    news.setArcHeight(20);
    news.setEffect(shadow);

    Text topStory = new Text("TOP STORY");
    topStory.setStyle("-fx-font-size: 20px;" +
        "-fx-font-weight: bold;");
    topStory.setFill(Color.RED);
    topStory.setTranslateX(530);
    topStory.setTranslateY(770);

    Text news1 = new Text("HOLIDAY WEEKEND OUTLOOK");
    news1.setStyle("-fx-font-size: 40px;");
    news1.setTranslateX(558);
    news1.setTranslateY(820);
    ImageView topNews = new ImageView(new Image("file:./res/news1.png"));
    topNews.setSmooth(true);
    topNews.setFitWidth(803);
    topNews.setFitHeight(396);
    topNews.setTranslateX(558);
    topNews.setTranslateY(840);

    topNews.setOnMouseClicked(event -> {
      setTitle(news1.getText());
      try {
        newsStage();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      primaryStage.close();
    });

    Text news12 = new Text("Here's Where Your Travel Could Be Slick");
    news12.setStyle("-fx-font-size: 24px;");
    news12.setTranslateX(558);
    news12.setTranslateY(1274);

    ImageView news2 = new ImageView(new Image("file:./res/news2.png"));
    news2.setSmooth(true);
    news2.setFitWidth(190);
    news2.setFitHeight(102);
    news2.setTranslateX(558);
    news2.setTranslateY(1305);
    Text news22 = new Text("Forecase For The Return Home\nThis Weekend");
    news22.setStyle("-fx-font-size: 14px");
    news22.setTranslateX(558);
    news22.setTranslateY(1428);

    ImageView news3 = new ImageView(new Image("file:./res/news3.png"));
    news3.setSmooth(true);
    news3.setFitWidth(190);
    news3.setFitHeight(102);
    news3.setTranslateX(762);
    news3.setTranslateY(1305);
    Text news33 = new Text("Live Colorada Weather Blog,\nDec. 5-7: Updates from\nDenver's Storm");
    news33.setStyle("-fx-font-size:14px;");
    news33.setTranslateX(762);
    news33.setTranslateY(1428);

    ImageView news4 = new ImageView(new Image("file:./res/news4.png"));
    news4.setSmooth(true);
    news4.setFitWidth(190);
    news4.setFitHeight(102);
    news4.setTranslateX(966);
    news4.setTranslateY(1305);
    Text news44 = new Text("Chicago First Alert Weather:\nCloudy Skies, Colder Temps");
    news44.setStyle("-fx-font-size:14px");
    news44.setTranslateX(966);
    news44.setTranslateY(1428);

    ImageView news5 = new ImageView(new Image("file:./res/news5.png"));
    news5.setSmooth(true);
    news5.setFitWidth(190);
    news5.setFitHeight(102);
    news5.setTranslateX(1171);
    news5.setTranslateY(1305);
    Text news55 = new Text("Orange County: Potential Rainfall");
    news55.setStyle("-fx-font-size:14px;");
    news55.setTranslateX(1171);
    news55.setTranslateY(1428);

    Image play = new Image("file:./res/play.png");

    ImageView play1 = new ImageView(play);
    play1.setSmooth(true);
    play1.setPreserveRatio(true);
    play1.setFitWidth(20);
    play1.setTranslateX(576);
    play1.setTranslateY(1500);
    Text play11 = new Text("Surprise Snowfall Inside A City Bus");
    play11.setStyle("-fx-font-size: 14;");
    play11.setTranslateX(608);
    play11.setTranslateY(1514);


    ImageView play2 = new ImageView(play);
    play2.setSmooth(true);
    play2.setPreserveRatio(true);
    play2.setFitWidth(20);
    play2.setTranslateX(576);
    play2.setTranslateY(1544);
    Text play22 = new Text("Southern California: Potential Snowfall");
    play22.setStyle("-fx-font-size: 14;");
    play22.setTranslateX(608);
    play22.setTranslateY(1560);
    Line ln2 = new Line(558, 1532, 1360, 1532);
    ln2.setOpacity(0.5);

    ImageView play3 = new ImageView(play);
    play3.setSmooth(true);
    play3.setPreserveRatio(true);
    play3.setFitWidth(20);
    play3.setTranslateX(576);
    play3.setTranslateY(1589);
    Text play33 = new Text("More Weather News");
    play33.setStyle("-fx-font-size: 14;");
    play33.setTranslateX(608);
    play33.setTranslateY(1606);
    Line ln3 = new Line(558, 1578, 1360, 1578);
    ln3.setOpacity(0.5);

    ImageView play4 = new ImageView(play);
    play4.setSmooth(true);
    play4.setPreserveRatio(true);
    play4.setFitWidth(20);
    play4.setTranslateX(576);
    play4.setTranslateY(1634);
    Text play44 = new Text("Lots of Snow Over There");
    play44.setStyle("-fx-font-size: 14;");
    play44.setTranslateX(608);
    play44.setTranslateY(1651);
    Line ln4 = new Line(558, 1624, 1360, 1624);
    ln4.setOpacity(0.5);

    ImageView play5 = new ImageView(play);
    play5.setSmooth(true);
    play5.setPreserveRatio(true);
    play5.setFitWidth(20);
    play5.setTranslateX(576);
    play5.setTranslateY(1679);
    Text play55 = new Text("Yup Still Kinda Warm In SoCal For Some Reason");
    play55.setStyle("-fx-font-size: 14;");
    play55.setTranslateX(608);
    play55.setTranslateY(1693);
    Line ln5 = new Line(558, 1669, 1360, 1669);
    ln5.setOpacity(0.5);

    Button seemore = new Button("See More");
    seemore.setPrefSize(96, 32);
    seemore.setTranslateX(533);
    seemore.setTranslateY(1728);
    seemore.setStyle(
        "-fx-text-fill: #FFFFFF;" +
            "-fx-background-color: #1B4DE4;" +
            "-fx-background-insets: 0, 1;" +
            "-fx-background-radius: 5em;" +
            "-fx-padding: 5px;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
        "-fx-content-display: center;" );

    // -------------------------------------------
    news1.setOnMouseClicked(event -> {
      setTitle(news1.getText());
      try {
        newsStage();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      primaryStage.close();
    });
    news2.setOnMouseClicked(event -> {
      setTitle(news22.getText());
      try {
        newsStage();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      primaryStage.close();
    });
    news3.setOnMouseClicked(event -> {
      setTitle(news33.getText());
      try {
        newsStage();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      primaryStage.close();
    });
    news4.setOnMouseClicked(event -> {
      setTitle(news44.getText());
      try {
        newsStage();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      primaryStage.close();
    });
    news5.setOnMouseClicked(event -> {
      setTitle(news55.getText());
      try {
        newsStage();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      primaryStage.close();
    });
    play1.setOnMouseClicked(event -> {
      setTitle(play11.getText());
      try {
        newsStage();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      primaryStage.close();
    });
    play2.setOnMouseClicked(event -> {
      setTitle(play22.getText());
      try {
        newsStage();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      primaryStage.close();
    });
    play3.setOnMouseClicked(event -> {
      setTitle(play33.getText());
      try {
        newsStage();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      primaryStage.close();
    });
    play4.setOnMouseClicked(event -> {
      setTitle(play44.getText());
      try {
        newsStage();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      primaryStage.close();
    });
    play5.setOnMouseClicked(event -> {
      setTitle(play55.getText());
      try {
        newsStage();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      primaryStage.close();
    });
    seemore.setOnMouseClicked(event -> {
      setTitle(news1.getText());
      try {
        newsStage();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      primaryStage.close();
    });
    // -----------------------------------------------------

    Pane newsBody = new Pane(readmore, news, topStory, news1, topNews, news12, news2, news22, news3, news33, news4, news44, news5, news55, 
        play1, play11, play2, play22, ln2, play3, play33, ln3, play4, play44, ln4, play5, play55, ln5, seemore);

    // ----------------------------------------------------------------

    Rectangle temp = new Rectangle(1920, 1800);
    temp.setFill(Color.web("#E8EEEE"));

    Pane frontBody = new Pane(temp , body, weatherMap, weatherToday, todayscast, today);

    Pane box = new Pane(frontBody, newsBody, top);

    LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
        new Stop(0, Color.web("#126992")),
        new Stop(1, Color.web("D8EEEE")));


    box.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(Color.web("E8EEEE"), null, null)));
    box.setPadding(new Insets(20));

    ScrollPane scrollPane = new ScrollPane(box);
    scrollPane.setFitToWidth(true);

    Scene scene = new Scene(scrollPane, 1920, 1000);
    primaryStage.setScene(scene);
    //primaryStage.setX(-10);
    //primaryStage.setY(0);
    primaryStage.show();
  }

  // SECOND
  
  private static void secondStage() {
    Stage stage = new Stage();
    stage.initStyle(StageStyle.UNDECORATED);

    String iconURL = "https://openweathermap.org/img/w/" + icon + ".png";
    ImageView icons = new ImageView(new Image(iconURL));

    DropShadow drop = new DropShadow();
    drop.setOffsetX(0);
    drop.setOffsetY(0);
    drop.setColor(Color.web("#C9C9C9"));

    // logo
    ImageView view = new ImageView();
    Image logos = new Image("file:./res/logo2.png");
    view.setImage(logos);
    view.setPreserveRatio(true);
    view.setSmooth(true);
    view.setFitWidth(57);
    view.setTranslateX(320);
    view.setTranslateY(11);

    view.setOnMouseClicked(event -> {
      try {
        restartApp(stage);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    });

    Text ibm = new Text("An IBM Business");
    ibm.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    ibm.setFill(Color.WHITE);
    ibm.setTranslateX(401);
    ibm.setTranslateY(45);

    TextField search = new TextField();
    search.setPromptText("Search City or Zip Code");
    search.setAlignment(Pos.CENTER);
    search.setPrefSize(380, 30);
    search.setTranslateX(705);
    search.setTranslateY(25);
    search.setStyle(
        "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-font-size: 14;" +
            "-fx-background-color: #337A9E;" +
        "-fx-text-fill: white;");

    search.setOnKeyPressed(e -> {
      if(e.getCode() == KeyCode.ENTER) {
        city = search.getText();
        try {
          APICall(city);
          secondStage();
          stage.close();
        } catch (URISyntaxException | IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });

    // search icon
    ImageView searcher = new ImageView();
    Image searched = new Image("file:./res/search.png");
    searcher.setImage(searched);
    searcher.setPreserveRatio(true);
    searcher.setSmooth(true);
    searcher.setFitWidth(20);
    searcher.setTranslateX(1052);
    searcher.setTranslateY(30);

    // header
    Rectangle header = new Rectangle();
    header.setWidth(1920);
    header.setHeight(85);
    header.setFill(Color.web("#005986"));

    // F / C
    ToggleButton fc = new ToggleButton("°C");
    fc.setPrefWidth(45);
    if(!toggle) {
      fc.setStyle(
          "-fx-base: #EF9E1C;" +
              "-fx-text-fill: #FFFFFF;" +
              "-fx-background-color: #82D1DB;" +
              "-fx-background-insets: 0, 1;" +
              "-fx-background-radius: 5em;" +
              "-fx-padding: 5px;" +
              "-fx-font-size: 14px;" +
              "-fx-content-display: center;"
          );
    } else {
      fc.setStyle(
          "-fx-background-color: #82D1DB;" +
              "-fx-text-fill: #FFFFFF;" +
              "-fx-background-insets: 0, 1;" +
              "-fx-background-radius: 5em;" +
              "-fx-padding: 5px;" +
              "-fx-font-size: 14px;" +
              "-fx-content-display: center;"
          );
    }
    fc.setOnAction(e -> {
      if(!(fc.isSelected())) {
        fc.setText("°F");
        toggle = false;
        fc.setStyle(
            "-fx-background-color: #EF9E1C;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-insets: 0, 1;" +
                "-fx-background-radius: 5em;" +
                "-fx-padding: 5px;" +
                "-fx-font-size: 14px;" +
                "-fx-content-display: center;"
            );
      }
      else {
        fc.setText("°C");
        fc.setStyle(
            "-fx-background-color: #82D1DB;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-insets: 0, 1;" +
                "-fx-background-radius: 5em;" +
                "-fx-padding: 5px;" +
                "-fx-font-size: 14px;" +
                "-fx-content-display: center;"
            );
      }
    });
    fc.setTranslateX(1325);
    fc.setTranslateY(25);

    // premium
    Button prem = new Button("GO PREMIUM");
    prem.setPrefSize(80, 30);
    prem.setStyle("-fx-background-color: #FFFFFF;" +
        "-fx-text-fill: #000000;" + 
        "-fx-background-insets: 0, 1;" +
        "-fx-background-radius: 5em;" +
        "-fx-padding: 5px;" +
        "-fx-font-size: 10px");
    prem.setTranslateX(1380);
    prem.setTranslateY(25);
    prem.setOnAction(event -> {
      premiumStage();
      stage.close();
    });

    Rectangle memRect = new Rectangle(1920, 40);
    memRect.setTranslateY(80);
    memRect.setFill(Color.web("#337A9E"));

    // remembering previous searches
    HBox mem = new HBox();
    mem.setPrefWidth(1920);
    mem.setPrefHeight(40);
    mem.setTranslateX(200);
    mem.setTranslateY(80);
    mem.setStyle("-fx-background-color: #337A9E;");

    // login
    ImageView login = new ImageView();
    Image logins = new Image("file:./res/login.png");
    login.setImage(logins);
    login.setPreserveRatio(true);
    login.setSmooth(true);
    login.setFitWidth(30);
    login.setTranslateX(1480);
    login.setTranslateY(25);

    login.setOnMouseClicked(event -> {
      loginStage();
    });

    // hamburger icon
    Line line1 = new Line(0, 0, 30, 0);
    Line line2 = new Line(0, 10, 30, 10);
    Line line3 = new Line(0, 20, 30, 20);
    Pane ham = new Pane(line1, line2, line3);
    for (Line line : new Line[]{line1, line2, line3}) {
      line.setStrokeWidth(3);
      line.setStroke(Color.WHITE);
    }
    ham.setTranslateX(1540);
    ham.setTranslateY(30);



    // FIFO
    HBox temporaryArray = new HBox();
    Label temporary = new Label("   " + temp + "\u00B0 " + city);
    temporary.setStyle("-fx-font-size: 18;");
    temporary.setTextFill(Color.WHITE);
    ImageView tempIcon = new ImageView(new Image("https://openweathermap.org/img/w/" + icon + ".png"));
    tempIcon.setPreserveRatio(true);
    tempIcon.setSmooth(true);
    tempIcon.setFitWidth(25);
    temporaryArray.getChildren().add(tempIcon);
    temporaryArray.getChildren().add(temporary);
    previous.add(temporaryArray);

    if(previous.size() > 4) {
      previous.remove(0);
    }

    mem.setAlignment(Pos.CENTER_LEFT);
    mem.setSpacing(40);
    for(int i = 0; i < previous.size(); i++) {
      HBox hboxTemp = previous.get(i);
      hboxTemp.setMinWidth(300);
      hboxTemp.setAlignment(Pos.CENTER);
      mem.getChildren().addAll(hboxTemp);

      if(i < previous.size() - 1) {
        Separator separator = new Separator();
        separator.setOrientation(javafx.geometry.Orientation.VERTICAL);
        mem.getChildren().addAll(separator);
      }
    }


    // keep this part since this basically holds the entire top portion of the site
    Pane top = new Pane();
    top.getChildren().addAll(header, view, ibm, fc, search, searcher, memRect, mem, prem, login);


    // ---------------------------------

    Rectangle timeBox = new Rectangle(900, 51);
    timeBox.setTranslateX(510);
    timeBox.setTranslateY(135);
    timeBox.setArcHeight(20);
    timeBox.setArcWidth(20);
    timeBox.setFill(Color.web("#0A4453"));
    Rectangle tempTimeBox = new Rectangle(900, 27);
    tempTimeBox.setTranslateX(510);
    tempTimeBox.setTranslateY(160);
    tempTimeBox.setFill(Color.web("#0A4453"));

    String currentCity = city + ", " + country + " As of " + timezone;
    Text location = new Text(currentCity);
    location.setStyle(
        "-fx-font-size: 20;" +
            "-fx-font-weight: bold;" 
        );
    location.setFill(Color.WHITE);
    location.setTranslateX(525);
    location.setTranslateY(168);

    DropShadow shadow = new DropShadow();
    shadow.setOffsetX(0);
    shadow.setOffsetY(0);
    shadow.setColor(Color.web("#696969"));

    Rectangle currentW = new Rectangle(900, 235);
    currentW.setTranslateX(510);
    currentW.setTranslateY(135);
    currentW.setArcWidth(20);
    currentW.setArcHeight(20);
    currentW.setFill(Color.web("#337A9E"));
    currentW.setEffect(drop);

    String tempo = temp + "\u00B0";
    Text tempText = new Text(tempo);
    tempText.setStyle("-fx-font-size: 98;");
    tempText.setFill(Color.WHITE);
    tempText.setTranslateX(525);
    tempText.setTranslateY(280);
    tempText.setEffect(shadow);

    Text descr = new Text(description);
    descr.setStyle("-fx-font-size: 24");
    descr.setFill(Color.WHITE);
    descr.setTranslateX(526);
    descr.setTranslateY(315);
    descr.setEffect(shadow);

    String stringhighLow = "High " + tempMax + "\u00B0 / Low " + tempMin + "\u00B0";
    Text highLow = new Text(stringhighLow);
    highLow.setStyle("-fx-font-size: 24;");
    highLow.setFill(Color.WHITE);
    highLow.setTranslateX(526);
    highLow.setTranslateY(350);

    icons.setSmooth(true);
    icons.setPreserveRatio(true);
    icons.setFitWidth(200);
    icons.setTranslateX(645);
    icons.setTranslateY(145);

    String stringfeelsLike = "Feels like " + feels + "\u00B0";
    Text feelsLike = new Text(stringfeelsLike);
    feelsLike.setStyle("-fx-font-size: 24;");
    feelsLike.setFill(Color.WHITE);
    String stringHum = "Humidity " + humidity + "%";
    Text hum = new Text(stringHum);
    hum.setStyle("-fx-font-size: 24;");
    hum.setFill(Color.WHITE);
    String stringWind = "Wind " + wind;
    if(toggle)
      stringWind += " kph";
    else
      stringWind += " mph";
    Text windy = new Text(stringWind);
    windy.setStyle("-fx-font-size: 24;");
    windy.setFill(Color.WHITE);
    String stringPres = "Pressure " + pressure + " inHg";
    Text pres = new Text(stringPres);
    pres.setStyle("-fx-font-size: 24;");
    pres.setFill(Color.WHITE);

    VBox etc = new VBox(10, feelsLike, hum, windy, pres);
    etc.setTranslateX(1020);
    etc.setTranslateY(195);

    Pane timePane = new Pane(currentW, timeBox, tempTimeBox, location, tempText, descr, highLow, icons, etc);

    // forecast time

    Rectangle forecastRectangle = new Rectangle(900, 640);
    forecastRectangle.setArcWidth(20);
    forecastRectangle.setArcHeight(20);
    forecastRectangle.setFill(Color.WHITE);
    forecastRectangle.setEffect(drop);
    Label forecastLabel = new Label("Today's Forecast for " + city);
    forecastLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
    Label morningLabel = new Label("Morning");
    morningLabel.setStyle("-fx-font-size: 20;");
    Label morningTemp = new Label(morning + "\u00B0");
    morningTemp.setStyle("-fx-font-size: 40;");
    morningTemp.setTextFill(Color.BLUE);
    ImageView morningImg = new ImageView(new Image(("https://openweathermap.org/img/w/" + morningIcon + ".png")));
    Label afternoonLabel = new Label("Afternoon");
    afternoonLabel.setStyle("-fx-font-size: 20;");
    Label afternoonTemp = new Label(afternoon + "\u00B0");
    afternoonTemp.setTextFill(Color.BLUE);
    afternoonTemp.setStyle("-fx-font-size: 40;");
    ImageView afternoonImg = new ImageView(new Image(("https://openweathermap.org/img/w/" + afternoonIcon + ".png")));
    Label eveningLabel = new Label("Evening");
    eveningLabel.setStyle("-fx-font-size: 20;");
    Label eveningTemp = new Label(evening + "\u00B0");
    eveningTemp.setTextFill(Color.BLUE);
    eveningTemp.setStyle("-fx-font-size: 40;");
    ImageView eveningImg = new ImageView(new Image(("https://openweathermap.org/img/w/" + eveningIcon + ".png")));
    Label nightLabel = new Label("Night");
    nightLabel.setStyle("-fx-font-size: 20;");
    Label nightTemp = new Label(night + "\u00B0");
    nightTemp.setStyle("-fx-font-size: 40;");
    nightTemp.setTextFill(Color.BLUE);
    ImageView nightImg = new ImageView(new Image(("https://openweathermap.org/img/w/" + nightIcon + ".png")));

    Insets insets = new Insets(10, 60, 10, 60);
    VBox morningBox = new VBox(morningLabel, morningTemp, morningImg);
    morningBox.setAlignment(Pos.CENTER);
    morningBox.setPadding(insets);
    VBox afternoonBox = new VBox(afternoonLabel, afternoonTemp, afternoonImg);
    afternoonBox.setAlignment(Pos.CENTER);
    afternoonBox.setPadding(insets);
    VBox eveningBox = new VBox(eveningLabel, eveningTemp, eveningImg);
    eveningBox.setAlignment(Pos.CENTER);
    eveningBox.setPadding(insets);
    VBox nightBox = new VBox(nightLabel, nightTemp, nightImg);
    nightBox.setAlignment(Pos.CENTER);
    nightBox.setPadding(insets);

    Separator forecastSep = new Separator(Orientation.VERTICAL);
    forecastSep.setOpacity(0.5);
    Separator forecastSep1 = new Separator(Orientation.VERTICAL);
    forecastSep.setOpacity(0.5);
    Separator forecastSep2 = new Separator(Orientation.VERTICAL);
    forecastSep.setOpacity(0.5);

    HBox forecastBox = new HBox(10, morningBox, forecastSep, afternoonBox, forecastSep1, eveningBox, forecastSep2, nightBox);
    forecastBox.setAlignment(Pos.CENTER);
    VBox forecastCont = new VBox(10, forecastLabel, forecastBox);
    forecastCont.setPadding(new Insets(15, 0, 0, 20));

    CategoryAxis x = new CategoryAxis();
    NumberAxis y = new NumberAxis(); 
    LineChart<String, Number> foreGraph = new LineChart<>(x, y);

    String[] xTicks = {"6", "9", "12", "15", "18", "21", "24"};
    x.setCategories(FXCollections.observableArrayList(Arrays.asList(xTicks)));
    foreGraph.getYAxis().setTickLabelsVisible(false);
    foreGraph.getYAxis().setOpacity(0);
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    foreGraph.setLegendVisible(false);
    for(int i = 0; i < forecastToday.size(); i++) {
      String h = xTicks[i];
      XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(h, forecastToday.get(i));
      Label dataLabel = new Label(forecastToday.get(i).toString());
      dataPoint.setNode(dataLabel);
      series.getData().add(dataPoint);
    }

    foreGraph.getData().add(series);

    VBox graphed = new VBox(10, forecastCont, foreGraph);

    StackPane forecastPane = new StackPane(forecastRectangle, graphed);
    forecastPane.setTranslateX(510);
    forecastPane.setTranslateY(387);

    // ---------------
    Label dailyFor = new Label("Daily Forecast");
    dailyFor.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
    Rectangle dailyRectangle = new Rectangle(900, 240);
    dailyRectangle.setArcWidth(20);
    dailyRectangle.setArcHeight(20);
    dailyRectangle.setFill(Color.WHITE);
    dailyRectangle.setEffect(drop);

    HBox forecastBoxed = new HBox();
    forecastBoxed.setSpacing(15);
    forecastBoxed.setAlignment(Pos.CENTER);
    for(int i = 0; i < forecastDates.size(); i++) {
      Label dateTemp = new Label(forecastDates.get(i));
      dateTemp.setStyle("-fx-font-size: 20;" + "-fx-font-weight: bold;");
      Label highTemp = new Label(forecastHigh.get(i).toString() + "\u00B0" + " / ");
      highTemp.setStyle("-fx-font-size: 32;");
      highTemp.setTextFill(Color.BLUE);
      Label lowTemp = new Label(forecastLow.get(i).toString() + "\u00B0");
      lowTemp.setStyle("-fx-font-size: 24");
      lowTemp.setTextFill(Color.BLUE);

      HBox tempBoxes = new HBox(highTemp, lowTemp);

      ImageView viewTemp = new ImageView(new Image(("https://openweathermap.org/img/w/" + forecastIcon.get(i) + ".png")));

      VBox boxedTemp = new VBox(dateTemp, tempBoxes, viewTemp);
      boxedTemp.setAlignment(Pos.CENTER);
      forecastBoxed.getChildren().addAll(boxedTemp);

      if(i < forecastDates.size() - 1) {
        Separator separator = new Separator();
        separator.setOrientation(javafx.geometry.Orientation.VERTICAL);
        forecastBoxed.getChildren().addAll(separator);
      }
    }
    VBox boxTime = new VBox(10, dailyFor, forecastBoxed);
    boxTime.setPadding(new Insets(15, 0, 0, 20));
    StackPane dailyPane = new StackPane(dailyRectangle, boxTime);
    dailyPane.setTranslateX(510);
    dailyPane.setTranslateY(1055);

    Rectangle radarRect = new Rectangle(900, 525);
    radarRect.setArcWidth(20);
    radarRect.setArcHeight(20);
    radarRect.setFill(Color.WHITE);
    radarRect.setEffect(drop);

    Label radarLabel = new Label("Radar");
    radarLabel.setStyle("-fx-font-size: 20;" + "-fx-font-weight: bold;");

    ImageView radar = new ImageView(new Image("file:./res/radar.png"));
    radar.setSmooth(true);
    radar.setFitWidth(900);
    radar.setFitHeight(464);

    VBox radarLabelPane = new VBox(radarLabel);
    radarLabelPane.setPadding(new Insets(10, 0, 0, 15));
    VBox radarBox = new VBox(10, radarLabelPane, radar);

    StackPane.setMargin(radarLabel, new Insets(10));
    StackPane stackRadar = new StackPane(radarRect, radarBox);
    stackRadar.setTranslateX(510);
    stackRadar.setTranslateY(1320);

    Rectangle temp = new Rectangle(1920, 1900);
    temp.setFill(Color.web("#E8EEEE"));

    Pane all = new Pane(temp, dailyPane, stackRadar, forecastPane, timePane, top);

    ScrollPane scrollPane = new ScrollPane(all);
    scrollPane.setFitToWidth(true);

    Scene scene = new Scene(scrollPane, 1920, 1000);
    stage.setScene(scene);
    stage.show();
  }

  // LOGIN

  private static void loginStage() {
    Stage loginStage = new Stage();

    DropShadow shadow = new DropShadow();
    shadow.setOffsetX(0);
    shadow.setOffsetY(0);
    shadow.setColor(Color.web("#C9C9C9"));

    Rectangle header = new Rectangle();
    header.setWidth(500);
    header.setHeight(80);
    header.setFill(Color.web("#005986"));

    Rectangle header2 = new Rectangle();
    header2.setWidth(500);
    header2.setHeight(40);
    header2.setTranslateY(80);
    String hexColor = "#337A9E";
    Color fillColor = Color.web(hexColor);
    header2.setFill(fillColor);

    ImageView view = new ImageView();
    Image logos = new Image("file:./res/logo2.png");
    view.setImage(logos);
    view.setPreserveRatio(true);
    view.setSmooth(true);
    view.setFitWidth(57);
    view.setTranslateX(11);
    view.setTranslateY(11);

    Text ibm = new Text("An IBM Business");
    ibm.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    ibm.setFill(Color.WHITE);
    ibm.setTranslateX(92);
    ibm.setTranslateY(45);

    Rectangle rect = new Rectangle(450, 330);
    rect.setFill(Color.WHITE);
    rect.setArcWidth(20);
    rect.setArcHeight(20);
    rect.setTranslateX(25);
    rect.setTranslateY(145);
    rect.setEffect(shadow);

    Group loginGroup = new Group();

    Text welcome = new Text("Log In");
    welcome.setStyle("-fx-font-size: 20;" +
        "-fx-font-weight: bold;");
    welcome.setTranslateX(225);
    welcome.setTranslateY(225);

    Label username = new Label("Username:");
    username.setTranslateX(160);
    username.setTranslateY(280);

    TextField usernameTF = new TextField();
    usernameTF.setTranslateX(225);
    usernameTF.setTranslateY(280);

    Label password = new Label("Password:");
    password.setTranslateX(160);
    password.setTranslateY(310);

    PasswordField pwf = new PasswordField();
    pwf.setTranslateX(225);
    pwf.setTranslateY(310);

    Button login = new Button("Log in");
    login.setPrefSize(96, 32);
    login.setStyle(
        "-fx-text-fill: #FFFFFF;" +
            "-fx-background-color: #1B4DE4;" +
            "-fx-background-insets: 0, 1;" +
            "-fx-background-radius: 5em;" +
            "-fx-padding: 5px;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
        "-fx-content-display: center;" );
    login.setTranslateX(280);
    login.setTranslateY(340);

    login.setOnAction(event -> {
      if(!usernameTF.getText().isEmpty() && !pwf.getText().isEmpty()) {
        loginStage.close();
      }
    });

    loginGroup.getChildren().addAll(welcome, username, usernameTF, password, pwf, login);

    Pane pane = new Pane(header, header2, rect, view, ibm, loginGroup);
    Pane box = new Pane(pane);
    box.setPadding(new Insets(20));

    loginStage.setTitle("Login");
    Scene loginScene = new Scene(box, 500, 500);
    loginStage.setScene(loginScene);
    loginStage.show();
  }

  //PREMIUM
  
  private static void premiumStage() {
    Stage premiumStage = new Stage();

    DropShadow drop = new DropShadow();
    drop.setOffsetX(0);
    drop.setOffsetY(0);
    drop.setColor(Color.web("#C9C9C9"));

    // logo
    ImageView view = new ImageView();
    Image logos = new Image("file:./res/logo2.png");
    view.setImage(logos);
    view.setPreserveRatio(true);
    view.setSmooth(true);
    view.setFitWidth(57);
    view.setTranslateX(320);
    view.setTranslateY(11);
    view.setOnMouseClicked(event -> {
      try {
        restartApp(premiumStage);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    });

    Text ibm = new Text("An IBM Business");
    ibm.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    ibm.setFill(Color.WHITE);
    ibm.setTranslateX(401);
    ibm.setTranslateY(45);

    TextField search = new TextField();
    search.setPromptText("Search City or Zip Code");
    search.setAlignment(Pos.CENTER);
    search.setPrefSize(380, 30);
    search.setTranslateX(705);
    search.setTranslateY(25);
    search.setStyle(
        "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-font-size: 14;" +
            "-fx-background-color: #337A9E;" +
        "-fx-text-fill: white;");

    search.setOnKeyPressed(e -> {
      if(e.getCode() == KeyCode.ENTER) {
        city = search.getText();
        //try {
        //	APICall(city);
        secondStage();
        Stage currentStage = (Stage) search.getScene().getWindow();
        currentStage.close();
        //} catch (URISyntaxException | IOException e1) {
        // TODO Auto-generated catch block
        //	e1.printStackTrace();
        //}
      }
    });

    // search icon
    ImageView searcher = new ImageView();
    Image searched = new Image("file:./res/search.png");
    searcher.setImage(searched);
    searcher.setPreserveRatio(true);
    searcher.setSmooth(true);
    searcher.setFitWidth(20);
    searcher.setTranslateX(1052);
    searcher.setTranslateY(30);

    // header
    Rectangle header = new Rectangle();
    header.setWidth(1920);
    header.setHeight(80);
    header.setFill(Color.web("#005986"));

    // F / C    	
    ToggleButton fc = new ToggleButton("°C");
    fc.setPrefWidth(45);
    if(!toggle) {
      fc.setStyle(
          "-fx-base: #EF9E1C;" +
              "-fx-text-fill: #FFFFFF;" +
              "-fx-background-color: #82D1DB;" +
              "-fx-background-insets: 0, 1;" +
              "-fx-background-radius: 5em;" +
              "-fx-padding: 5px;" +
              "-fx-font-size: 14px;" +
              "-fx-content-display: center;"
          );
    } else {
      fc.setStyle(
          "-fx-background-color: #82D1DB;" +
              "-fx-text-fill: #FFFFFF;" +
              "-fx-background-insets: 0, 1;" +
              "-fx-background-radius: 5em;" +
              "-fx-padding: 5px;" +
              "-fx-font-size: 14px;" +
              "-fx-content-display: center;"
          );
    }
    fc.setOnAction(e -> {
      if(fc.isSelected()) {
        fc.setText("°F");
        toggle = false;
        fc.setStyle(
            "-fx-background-color: #EF9E1C;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-insets: 0, 1;" +
                "-fx-background-radius: 5em;" +
                "-fx-padding: 5px;" +
                "-fx-font-size: 14px;" +
                "-fx-content-display: center;"
            );
      }
      else {
        fc.setText("°C");
        toggle = true;
        fc.setStyle(
            "-fx-background-color: #82D1DB;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-insets: 0, 1;" +
                "-fx-background-radius: 5em;" +
                "-fx-padding: 5px;" +
                "-fx-font-size: 14px;" +
                "-fx-content-display: center;"
            );
      }
    });
    fc.setTranslateX(1325);
    fc.setTranslateY(25);

    // premium
    Button prem = new Button("GO PREMIUM");
    prem.setPrefSize(80, 30);
    prem.setStyle("-fx-background-color: #FFFFFF;" +
        "-fx-text-fill: #000000;" + 
        "-fx-background-insets: 0, 1;" +
        "-fx-background-radius: 5em;" +
        "-fx-padding: 5px;" +
        "-fx-font-size: 10px");
    prem.setTranslateX(1380);
    prem.setTranslateY(25);
    prem.setOnAction(event -> {
      premiumStage();
      premiumStage.close();
    });

    // remembering previous searches
    VBox mem = new VBox();
    mem.setPrefWidth(1920);
    mem.setPrefHeight(40);
    mem.setTranslateY(80);
    mem.setStyle("-fx-background-color: #337A9E;");

    // login
    ImageView login = new ImageView();
    Image logins = new Image("file:./res/login.png");
    login.setImage(logins);
    login.setPreserveRatio(true);
    login.setSmooth(true);
    login.setFitWidth(30);
    login.setTranslateX(1480);
    login.setTranslateY(25);

    // hamburger icon
    Line line1 = new Line(0, 0, 30, 0);
    Line line2 = new Line(0, 10, 30, 10);
    Line line3 = new Line(0, 20, 30, 20);
    Pane ham = new Pane(line1, line2, line3);
    for (Line line : new Line[]{line1, line2, line3}) {
      line.setStrokeWidth(3);
      line.setStroke(Color.WHITE);
    }
    ham.setTranslateX(1540);
    ham.setTranslateY(30);

    // keep this part since this basically holds the entire top portion of the site
    Pane top = new Pane();
    top.getChildren().addAll(header, view, ibm, fc, search, searcher, mem, prem, login);

    // event handlers for other buttons

    login.setOnMouseClicked(event -> {
      loginStage();
    });

    // text
    Text gop = new Text("Go Premium");
    gop.setStyle("-fx-font-size: 25;" +
        "-fx-font-weight: lighter;");
    Text price = new Text("$4.99 / MONTH");
    price.setStyle("-fx-font-size: 30;" + 
        "-fx-font-weight: bold;");
    Button sub = new Button("Subscribe");
    sub.setPrefSize(300, 50);
    sub.setStyle(
        "-fx-text-fill: #FFFFFF;" +
            "-fx-background-color: #1B4DE4;" +
            "-fx-background-insets: 0, 1;" +
            "-fx-background-radius: 5em;" +
            "-fx-padding: 5px;" +
            "-fx-font-size: 17px;" +
            "-fx-font-weight: bold;" +
        "-fx-content-display: center;" );
    Text ads = new Text("• No Ads");
    ads.setStyle("-fx-font-size: 20;" + 
        "-fx-font-weight: normal;");
    Text benefit = new Text("• 72-Hour Future Radar");
    benefit.setStyle("-fx-font-size: 20;" + 
        "-fx-font-weight: normal;");
    Text benefit1 = new Text("• Windstream");
    benefit1.setStyle("-fx-font-size: 20;" + 
        "-fx-font-weight: normal;");
    Text benefit2 = new Text("• 192-Hour Forecast");
    benefit2.setStyle("-fx-font-size: 20;" + 
        "-fx-font-weight: normal;");
    Text benefit3 = new Text("• Extended 15-Minute Details");
    benefit3.setStyle("-fx-font-size: 20;" + 
        "-fx-font-weight: normal;");
    Text benefit4 = new Text("• Lightning and Snowfall");
    benefit4.setStyle("-fx-font-size: 20;" + 
        "-fx-font-weight: normal;");

    Alert alert = new Alert(AlertType.CONFIRMATION);
    PauseTransition delay = new PauseTransition(Duration.millis(5000));

    sub.setOnAction(event -> {
      alert.setContentText("You have subscribed to Premium! You will be redirected"
          + " to the home page in 5 seconds.");
      delay.play();
      alert.show();
    });

    delay.setOnFinished(event -> {
      try {
        restartApp(premiumStage);
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    });

    // --------------------------------------

    Pane text = new Pane();
    VBox benefitsBox = new VBox(10, ads, benefit, benefit1,
        benefit2, benefit3, benefit4);
    benefitsBox.setAlignment(Pos.CENTER_LEFT);
    VBox vbox = new VBox(10, gop, price, sub, benefitsBox);
    vbox.setLayoutX(810);
    vbox.setLayoutY(205);
    vbox.setAlignment(Pos.CENTER);
    text.getChildren().add(vbox);

    Rectangle rect = new Rectangle(450, 475);
    rect.setFill(Color.WHITE);
    rect.setArcWidth(20);
    rect.setArcHeight(20);
    rect.setTranslateX(735);
    rect.setTranslateY(168);
    rect.setEffect(drop);

    Pane box = new Pane(rect, text, top);
    //Pane box = new Pane(pane);
    box.setPadding(new Insets(20));

    premiumStage.setTitle("Go Premium");
    Scene premiumScene = new Scene(box, 1920, 1000);
    premiumStage.setScene(premiumScene);
    premiumStage.show();
  }

  private static void newsStage() throws Exception {
    Stage newsStage = new Stage();

    DropShadow drop = new DropShadow();
    drop.setOffsetX(0);
    drop.setOffsetY(0);
    drop.setColor(Color.web("#C9C9C9"));

    // logo
    ImageView view = new ImageView();
    Image logos = new Image("file:./res/logo2.png");
    view.setImage(logos);
    view.setPreserveRatio(true);
    view.setSmooth(true);
    view.setFitWidth(57);
    view.setTranslateX(320);
    view.setTranslateY(11);
    view.setOnMouseClicked(event -> {
      try {
        restartApp(newsStage);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    });

    Text ibm = new Text("An IBM Business");
    ibm.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    ibm.setFill(Color.WHITE);
    ibm.setTranslateX(401);
    ibm.setTranslateY(45);

    TextField search = new TextField();
    search.setPromptText("Search City or Zip Code");
    search.setAlignment(Pos.CENTER);
    search.setPrefSize(380, 30);
    search.setTranslateX(705);
    search.setTranslateY(25);
    search.setStyle(
        "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-font-size: 14;" +
            "-fx-background-color: #337A9E;" +
        "-fx-text-fill: white;");

    search.setOnKeyPressed(e -> {
      if(e.getCode() == KeyCode.ENTER) {
        city = search.getText();
        //try {
        //	APICall(city);
        secondStage();
        Stage currentStage = (Stage) search.getScene().getWindow();
        currentStage.close();
        //} catch (URISyntaxException | IOException e1) {
        // TODO Auto-generated catch block
        //	e1.printStackTrace();
        //}
      }
    });

    // search icon
    ImageView searcher = new ImageView();
    Image searched = new Image("file:./res/search.png");
    searcher.setImage(searched);
    searcher.setPreserveRatio(true);
    searcher.setSmooth(true);
    searcher.setFitWidth(20);
    searcher.setTranslateX(1052);
    searcher.setTranslateY(30);

    // header
    Rectangle header = new Rectangle();
    header.setWidth(1920);
    header.setHeight(80);
    header.setFill(Color.web("#005986"));

    // F / C    	
    ToggleButton fc = new ToggleButton("°C");
    fc.setPrefWidth(45);
    if(!toggle) {
      fc.setStyle(
          "-fx-base: #EF9E1C;" +
              "-fx-text-fill: #FFFFFF;" +
              "-fx-background-color: #82D1DB;" +
              "-fx-background-insets: 0, 1;" +
              "-fx-background-radius: 5em;" +
              "-fx-padding: 5px;" +
              "-fx-font-size: 14px;" +
              "-fx-content-display: center;"
          );
    } else {
      fc.setStyle(
          "-fx-background-color: #82D1DB;" +
              "-fx-text-fill: #FFFFFF;" +
              "-fx-background-insets: 0, 1;" +
              "-fx-background-radius: 5em;" +
              "-fx-padding: 5px;" +
              "-fx-font-size: 14px;" +
              "-fx-content-display: center;"
          );
    }
    fc.setOnAction(e -> {
      if(fc.isSelected()) {
        fc.setText("°F");
        toggle = false;
        fc.setStyle(
            "-fx-background-color: #EF9E1C;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-insets: 0, 1;" +
                "-fx-background-radius: 5em;" +
                "-fx-padding: 5px;" +
                "-fx-font-size: 14px;" +
                "-fx-content-display: center;"
            );
      }
      else {
        fc.setText("°C");
        toggle = true;
        fc.setStyle(
            "-fx-background-color: #82D1DB;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-insets: 0, 1;" +
                "-fx-background-radius: 5em;" +
                "-fx-padding: 5px;" +
                "-fx-font-size: 14px;" +
                "-fx-content-display: center;"
            );
      }
    });
    fc.setTranslateX(1325);
    fc.setTranslateY(25);

    // premium
    Button prem = new Button("GO PREMIUM");
    prem.setPrefSize(80, 30);
    prem.setStyle("-fx-background-color: #FFFFFF;" +
        "-fx-text-fill: #000000;" + 
        "-fx-background-insets: 0, 1;" +
        "-fx-background-radius: 5em;" +
        "-fx-padding: 5px;" +
        "-fx-font-size: 10px");
    prem.setTranslateX(1380);
    prem.setTranslateY(25);
    prem.setOnAction(event -> {
      premiumStage();
      newsStage.close();
    });

    // remembering previous searches
    VBox mem = new VBox();
    mem.setPrefWidth(1920);
    mem.setPrefHeight(40);
    mem.setTranslateY(80);
    mem.setStyle("-fx-background-color: #337A9E;");

    // login
    ImageView login = new ImageView();
    Image logins = new Image("file:./res/login.png");
    login.setImage(logins);
    login.setPreserveRatio(true);
    login.setSmooth(true);
    login.setFitWidth(30);
    login.setTranslateX(1480);
    login.setTranslateY(25);

    // hamburger icon
    Line line1 = new Line(0, 0, 30, 0);
    Line line2 = new Line(0, 10, 30, 10);
    Line line3 = new Line(0, 20, 30, 20);
    Pane ham = new Pane(line1, line2, line3);
    for (Line line : new Line[]{line1, line2, line3}) {
      line.setStrokeWidth(3);
      line.setStroke(Color.WHITE);
    }
    ham.setTranslateX(1540);
    ham.setTranslateY(30);

    // keep this part since this basically holds the entire top portion of the site
    Pane top = new Pane();
    top.getChildren().addAll(header, view, ibm, fc, search, searcher, mem, prem, login);

    // event handlers for other buttons

    login.setOnMouseClicked(event -> {
      loginStage();
    });

    // news
    Rectangle news = new Rectangle(900, 700);
    news.setFill(Color.WHITE);
    news.setTranslateX(510);
    news.setTranslateY(143);
    news.setArcWidth(20);
    news.setArcHeight(20);
    news.setEffect(drop);

    Text title = new Text(getTitle());
    title.setStyle("-fx-font-size: 24px;" + 
        "-fx-font-weight: bold;");
    title.setTranslateX(560);
    title.setTranslateY(193);

    String str = "";
    FileReader fr = new FileReader("./res/lorem");
    int c;
    while((c = fr.read()) != -1) {
      str += ((char)c);
    }
    fr.close();

    Text lorem = new Text();
    lorem.setText(str);
    lorem.setTranslateX(560);
    lorem.setTranslateY(243);
    lorem.setStyle("-fx-font-size: 20px;" +
        "-fx-font-weight: lighter;");
    lorem.setWrappingWidth(800);


    Pane box = new Pane(top, news, title, lorem);
    //Pane box = new Pane(pane);
    box.setPadding(new Insets(20));

    Scene newsScene = new Scene(box, 1920, 1000);
    newsStage.setScene(newsScene);
    newsStage.show();
  }

  private static void setTitle(String t) {
    t = t.replace("\n", " ");
    title = t;

  }
  private static String getTitle() {
    return title;
  }

  private static void restartApp(Stage primaryStage) throws Exception {
    Stage newStage = new Stage();
    newStage.initStyle(StageStyle.UNDECORATED);
    //newStage.setX(primaryStage.getX() - 10);
    uiStart(newStage);
    PauseTransition delay = new PauseTransition(Duration.millis(500));
    delay.setOnFinished(e -> {
      primaryStage.close();
    });
    delay.play();
  }

  // method to call API and store the resulting JSON data
  public static void APICall(String city) throws URISyntaxException, IOException {

    // defaults to metric unless specified
    String apiURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + map  + "&units=metric";
    String forecastURL = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + map + "&units=metric";

    if(!toggle) {
      // imperial data
      apiURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + map + "&units=imperial";
      forecastURL = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + map + "&units=imperial";
    }

    // establishes connection with daily API
    URL url = new URL(apiURL);
    URLConnection connection = url.openConnection();
    BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    StringBuilder jsn = new StringBuilder();

    // reading JSON data
    String ln;
    while((ln = rd.readLine()) != null)
      jsn.append(ln);
    ln = "";
    rd.close();

    // extracting current day data
    JSONObject data = new JSONObject(jsn.toString());

    //System.out.println(data);

    icon = data.getJSONArray("weather").getJSONObject(0).getString("icon");
    humidity = data.getJSONObject("main").getInt("humidity");
    wind = data.getJSONObject("wind").getInt("speed");
    description = data.getJSONArray("weather").getJSONObject(0).getString("description");
    temp = data.getJSONObject("main").getInt("temp");
    tempMax = data.getJSONObject("main").getInt("temp_max");
    tempMin = data.getJSONObject("main").getInt("temp_min");
    feels = data.getJSONObject("main").getInt("feels_like");
    sunrise = data.getJSONObject("sys").getInt("sunrise");
    sunset = data.getJSONObject("sys").getInt("sunset");
    country = data.getJSONObject("sys").getString("country");
    pressure = data.getJSONObject("main").getInt("pressure");
    double pressureTemp = pressure * 0.02952998751;

    DecimalFormat df = new DecimalFormat("#.##");
    pressure = Double.parseDouble(df.format(pressureTemp));

    int timeTemp = data.getInt("timezone");
    long currentTime = System.currentTimeMillis();
    Instant inst = Instant.ofEpochMilli(currentTime);
    ZoneId zone = ZoneId.ofOffset("UTC" , ZoneOffset.ofTotalSeconds(timeTemp));
    ZonedDateTime zoned = ZonedDateTime.ofInstant(inst, zone);
    DateTimeFormatter timez = DateTimeFormatter.ofPattern("HH:mm");
    timezone = zoned.format(timez);

    // JSON data parsing complete


    // establishes connection with forecast API
    url = new URL(forecastURL);
    connection = url.openConnection();
    rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    jsn = new StringBuilder();

    // reading JSON data
    while((ln = rd.readLine()) != null)
      jsn.append(ln);
    ln = "";
    rd.close();

    // extracting forecast data
    JSONArray forecastData = new JSONObject(jsn.toString()).getJSONArray("list");
    Map<String, JSONObject> checkDate = new TreeMap<>();

    //System.out.println(forecastData);

    forecastToday.clear();

    // TreeMap to get only one weather per date because it returns data for every 3 hrs
    for(int i = 0; i < forecastData.length(); i++) {
      JSONObject day = forecastData.getJSONObject(i);
      String date = day.getString("dt_txt").split(" ")[0];
      String dt = day.getString("dt_txt").split(" ")[1];

      //System.out.println(dt);
      //System.out.println(day.getJSONObject("main").getDouble("temp"));

      if(!checkDate.containsKey(date))
        checkDate.put(date, day);

      // gathering morning, afternoon, evening, night weather
      if(i < 7) {
        System.out.println(i);
        forecastToday.add((int) day.getJSONObject("main").getDouble("temp"));
        if(dt.endsWith("06:00:00")) {
          morning = (int) day.getJSONObject("main").getDouble("temp");
          morningIcon = day.getJSONArray("weather").getJSONObject(0).getString("icon");
          System.out.println("morning");
        }
        else if(dt.endsWith("12:00:00")) {
          afternoon = (int) day.getJSONObject("main").getDouble("temp");
          afternoonIcon = day.getJSONArray("weather").getJSONObject(0).getString("icon");
          System.out.println("morning");
        }
        else if(dt.endsWith("18:00:00")) {
          evening = (int) day.getJSONObject("main").getDouble("temp");
          eveningIcon = day.getJSONArray("weather").getJSONObject(0).getString("icon");
          System.out.println("morning");
        }
        else if(dt.endsWith("00:00:00")) {
          night = (int) day.getJSONObject("main").getDouble("temp");
          nightIcon = day.getJSONArray("weather").getJSONObject(0).getString("icon");
          System.out.println("morning");
        }
      }
    }


    System.out.println(forecastData);
    System.out.println(night);

    forecastTemp.clear();
    forecastDescr.clear();
    forecastHigh.clear();
    forecastLow.clear();
    forecastDates.clear();
    forecastIcon.clear();

    // extracting forecast daily data
    for (Map.Entry<String, JSONObject> entry : checkDate.entrySet()) {
      int temperature = entry.getValue().getJSONObject("main").getInt("temp");
      String desc = entry.getValue().getJSONArray("weather").getJSONObject(0).getString("description");
      int high = entry.getValue().getJSONObject("main").getInt("temp_max");
      int low = entry.getValue().getJSONObject("main").getInt("temp_min");
      String date = entry.getKey();
      date = date.substring(5);

      String iconic = entry.getValue().getJSONArray("weather").getJSONObject(0).getString("icon");

      forecastTemp.add(temperature);
      forecastDescr.add(desc);
      forecastHigh.add(high);
      forecastLow.add(low);
      forecastDates.add(date);
      forecastIcon.add(iconic);
    }
    /*
      // checking to validate data
        for (Map.Entry<String, JSONObject> entry : checkDate.entrySet()) {
            System.out.println("Date: " + entry.getKey());
            System.out.println(entry.getValue().toString(2));
        }
        for(int i = 0; i < forecastTemp.size(); i++) {
          System.out.println("Temp: " + forecastTemp.get(i));
          System.out.println("Description: " + forecastDescr.get(i));
          System.out.println("High: " + forecastHigh.get(i));
          System.out.println("Low: " + forecastLow.get(i));
          System.out.println("Date: " + forecastDates.get(i));
        }
     */
  }
}
