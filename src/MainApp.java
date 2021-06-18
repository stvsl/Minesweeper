import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    // 按钮组，逻辑数据组和常量组和必须控件
    // ArrayList<Box> bts = new ArrayList<Box>();
    Button[][] bts = new Button[32][64];
    int[][] ans = new int[32][64];
    Label expression = new Label();
    static String[][] btString = new String[32][64];
    boolean[][] isOpened = new boolean[32][64];
    static int count = 0;
    static boolean isFirst = true;
    Label boomNums = new Label();
    Button revButton = new Button("重新开始");
    static int ii, jj;
    static int spread_Counter = 0;

    @Override
    public void start(Stage root) throws Exception {
        initialization(bts, ans,isOpened);
        GridPane gp = new GridPane();
        for (int i = 0; i < bts.length; i++) {
            for (int j = 0; j < bts[i].length; j++) {
                gp.add(bts[i][j], (j + 2), (i + 1));
            }
        }
        boomNums.setText("炸弹数目：" + count);
        expression.setText("~^_^~");
        gp.add(boomNums, 0, 0);
        gp.add(revButton, 0, 1);
        gp.add(expression,0,3);

        // 重置按钮功能实现
        revButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                initialization(bts, ans,isOpened);
                boomNums.setText("炸弹数目：" + count);
            }

        });

        // 为每个按钮生成事件
        for (ii = 0; ii < bts.length; ii++) {
            for (jj = 0; jj < bts[ii].length; jj++) {
                bts[ii][jj].setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent arg0) {
                        // 获取按钮内存地址
                        // System.out.println(arg0.getTarget());
                        String tmp = arg0.getTarget().toString();
                        int irun = 0, jrun = 0;
                        // 地址匹配推算按钮编号
                        for (int i = 0; i < bts.length; i++) {
                            for (int j = 0; j < bts[i].length; j++) {
                                if (tmp.equals(btString[i][j])) {
                                    irun = i;
                                    jrun = j;
                                }
                            }
                        }
                       //System.out.println("点击的按钮是："+ irun + "\t"+ jrun);
                        // 校验是不是炸弹
                        if (ans[irun][jrun] == 1) {
                            //游戏结束
                            gameOver(bts,ans);
                        } else {
                            //触发游戏事件
                            gameEvent(irun, jrun, bts, ans);
                        }

                    }

                });
            }
        }

        Scene scene = new Scene(gp, 1760, 850);
        root.setTitle("扫雷地狱版");
        root.setScene(scene);
        root.show();
    }

    //javafx边缘启动器
    public static void main(String[] args) {
        Application.launch(args);
    }

    // 游戏初始化
    public static void initialization(Button[][] bts, int[][] ans,boolean[][] isOpened) {
        System.out.println("开始初始化");
        // 判断是否是第一次执行
        if (isFirst == true) {
            // 初始化button数组
            for (int i = 0; i < bts.length; i++) {
                for (int j = 0; j < bts[i].length; j++) {
                    bts[i][j] = new Button();
                    bts[i][j].setMaxSize(25, 25);
                    bts[i][j].setMinSize(25, 25);
                    bts[i][j].setStyle("-fx-background:white");
                    btString[i][j] = bts[i][j].toString();
                    isOpened[i][j] = false;
                }
            }
            isFirst = false;
        } else {
            // 重置按钮状态
            for (int i = 0; i < bts.length; i++) {
                for (int j = 0; j < bts[i].length; j++) {
                    bts[i][j].setStyle("-fx-background:white");
                    bts[i][j].setText("");
                    //bts[i][j].setDisable(false);
                }
            }
        }
        count = 0;
        // 初始化游戏依赖数据
        while (count < 1000) {
            for (int i = 0; i < ans.length; i++) {
                for (int j = 0; j < ans[i].length; j++) {
                    int tmp = (int) (Math.random() * 1.15);
                    // System.out.println(tmp);
                    ans[i][j] = tmp;
                    if (tmp == 1) {
                        count++;
                    }
                    if (count == 1000) {
                        break;
                    }
                }
            }
        }
    }
    //点到炸弹执行
    public static void gameOver(Button[][] bts, int[][] ans) {
        // 是炸弹就让所有炸弹按钮同时爆炸
        for (int i = 0; i < bts.length; i++) {
            for (int j = 0; j < bts[i].length; j++) {
                if (ans[i][j] == 1) {
                    bts[i][j].setStyle("-fx-background-color:red;-fx-background:white");
                }
            }
        }
        // 弹出失败提示框
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("哈哈哈");
        alert.setHeaderText("你踩到炸弹了！！！");
        alert.setContentText("重新开始吧～");
        alert.show();
        // initialization(bts, ans);
    }

    //普通按钮触发事件
    public static void gameEvent(int irun,int jrun,Button[][] bts,int[][] ans){
        int boom_Number = 0;
        //判断是不是边界按钮
        if (irun == 0 || jrun == 0 || jrun == bts[irun].length-1 || irun == bts.length-1) {
            boom_Number = Edge_button(irun,jrun,bts,ans);
        } else {
            for (int i = irun - 1; i <= irun + 1; i++) {
                for (int j = jrun - 1; j <= jrun + 1; j++) {
                    if (ans[i][j] == 1) {
                        boom_Number++;
                    }
                }
            }
        }
        bts[irun][jrun].setStyle("-fx-background:white;-fx-background-color:white;-fx-font: 12 arial;-fx-text-fill:black;-fx-font-weight:bold");
        if(boom_Number != 0) {
            bts[irun][jrun].setText(Integer.toString(boom_Number));
        }
        //bts[irun][jrun].setDisable(true);

    }

    //特殊事件——如果按钮在边缘普通炸弹运算无法正常直接计算的各种场合
    public static int Edge_button(int irun,int jrun,Button[][] bts,int[][] ans){
        int boom_Number = 0;
        //0 0 点边界
        if (irun == 0 && jrun == 0){
            for (int i = 0; i <= 1;i++){
                for (int j = 0; j <= 1; j++){
                    if(ans[i][j] == 1){
                        boom_Number++;
                    }
                }
            }
        }
        //0 x 边界
        else if(irun == 0 && jrun == bts[irun].length - 1){
            System.out.println("2");
            for (int i = 0; i <= irun + 1; i++){
                for (int j = (jrun-1); j <= jrun;j++){
                    if(ans[i][j] == 1){
                        boom_Number++;
                    }
                }
            }
        }
        //x 0 边界
        else if(irun == bts.length - 1 && jrun == 0){
            for (int i = irun - 1; i <= irun; i++){
                for (int j = 0; j <= 1; j++){
                    if(ans[i][j] == 1){
                        boom_Number++;
                    }
                }
            }
        }
        //x x 边界
        else if(irun == bts.length - 1&& jrun == bts[irun].length - 1){
            for (int i = irun - 1; i <= irun; i++){
                for (int j = jrun - 1;j <= jrun; j++){
                    if(ans[i][j] == 1){
                        boom_Number++;
                    }
                }
            }
        }
        //左排边界
        else if(jrun == 0){
            for (int i = irun - 1; i <= irun + 1; i++){
                for (int j = 0 ;j <= 1 ; j++){
                    if(ans[i][j] == 1){
                        boom_Number++;
                    }
                }
            }
        }
        //顶排边界
        else if(irun == 0){
           for (int i = 0; i <= 1; i++){
               for (int j = jrun -1 ; j <= jrun + 1; j++){
                   if (ans[i][j] == 1) {
                       boom_Number++;
                   }
               }
           }
        }
        //底排边界
        else if (irun == bts.length - 1){
            for (int i = irun - 1; i <= irun;i++){
                for (int j = jrun - 1 ; j <= jrun + 1;j++){
                    if (ans[i][j] == 1) {
                        boom_Number++;
                    }
                }
            }
        }
        //右排边界
        else if (jrun == bts[irun].length - 1){
            for (int i = irun - 1; i <= irun; i++){
                for (int j = jrun -1;j <= jrun;j++){
                    if (ans[i][j] == 1){
                        boom_Number++;
                    }
                }
            }
        }
        return boom_Number;
    }
    
    //蔓延算法，向附近9个按钮发送自动点击请求
    public static void spread(int irun,int jrun,Button[][] bts,int[][] ans,boolean[][] isOpened) {
        /*
        此处描述法为    7 8 9  将其对应转化为  (irun-1,jrun-1) (irun-1,jrun) (irun-1,jrun+1)
                        4 5 6                   (irun,jrun-1)   (irun,jrun)   (irun,jrun+1)
                        1 2 3                  (irun+1,jrun-1) (irun+1,jrun) (irun+1,jrun+1)
        然后进行判定是否该处被打开，若被打开则不对其蔓延，否则执行蔓延运算方法
         */

    }

    //
    public static void spread_Runner(int irun,int jrun,Button[][] bts,int[][] ans,boolean[][] isOpened){
        if (irun != 0||jrun != 0||irun != bts.length - 1||jrun != bts[irun].length - 1){
            
        }
    }

}
