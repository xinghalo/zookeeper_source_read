package book.chapter05.$5_3_2;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author xinghailong at 2020-09-22 3:51 下午
 */
public class ZooKeeper_Create_API_Sync_Usage implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper(
                "localhost:2181",
                5000,
                new ZooKeeper_Create_API_Sync_Usage());

        connectedSemaphore.await();

        // 创建临时节点
        String path1 = zooKeeper.create(
                "/zk-test-ephemeral-",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL
        );
        System.out.println("success create znode: " + path1);

        // 创建临时顺序节点
        String path2 = zooKeeper.create(
                "/zk-test-ephemeral-",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL
        );
        System.out.println("success create znode: " + path2);
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("receive watched event: " + event);
        if(event.getState() == Event.KeeperState.SyncConnected){
            connectedSemaphore.countDown();
        }
    }
}
