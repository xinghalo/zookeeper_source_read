package time;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author xinghailong at 2020-09-24 5:27 下午
 */
public class TimeTest {
    public static void main(String[] args) throws InterruptedException {

        Timer timer = new Timer("test");

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("任务执行 - " + LocalDateTime.now());
                // 异常之后就不会再执行了
                System.out.println(1/0);
            }
        };

        timer.scheduleAtFixedRate(task, 0, TimeUnit.SECONDS.toMillis(1));

        Thread.sleep(Integer.MAX_VALUE);
    }
}
