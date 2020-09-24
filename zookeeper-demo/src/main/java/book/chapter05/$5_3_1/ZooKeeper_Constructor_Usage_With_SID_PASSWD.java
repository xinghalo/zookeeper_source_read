package book.chapter05.$5_3_1;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 创建连接，注册监听器
 *
 * @author xinghailong at 2020-09-22 1:14 下午
 */
public class ZooKeeper_Constructor_Usage_With_SID_PASSWD implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(3);

    public static void main(String[] args) throws IOException, InterruptedException {

        // 客户端异步建立连接，因此需要使用countDownLatch等待
        ZooKeeper zooKeeper = new ZooKeeper(
                "localhost:2181", // zk服务器列表，
                5000, // session超时时间
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD() // 一次性的注册事件
        );

        long sessionId = zooKeeper.getSessionId();
        byte[] passwd = zooKeeper.getSessionPasswd();

        // 非法的sessionId和sessionPasswd
        zooKeeper = new ZooKeeper(
                "localhost:2181", // zk服务器列表，
                5000, // session超时时间
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD(), // 一次性的注册事件
                1l,
                "test".getBytes()
        );

        // 正确的sessionId和sessionPasswd
        zooKeeper = new ZooKeeper(
                "localhost:2181", // zk服务器列表，
                5000, // session超时时间
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD(), // 一次性的注册事件
                sessionId,
                passwd
        );

        connectedSemaphore.await();
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("receive watched event: " + event);
        if(event.getState() == Event.KeeperState.SyncConnected){
            connectedSemaphore.countDown();
        }
    }
}
