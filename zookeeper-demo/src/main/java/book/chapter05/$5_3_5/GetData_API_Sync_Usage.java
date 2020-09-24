package book.chapter05.$5_3_5;

import book.chapter05.$5_3_4.ZooKeeper_GetChildren_API_Sync_Usage;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author xinghailong at 2020-09-22 4:33 下午
 */
public class GetData_API_Sync_Usage implements Watcher {
    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper zk;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String path = "/zk-book3";

        zk = new ZooKeeper(
                "localhost:2181",
                5000,
                new GetData_API_Sync_Usage()
        );

        latch.await();

        zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        System.out.println(new String(zk.getData(path, true, stat)));
        System.out.println(stat.getCzxid() + "," + stat.getMzxid()+"，"+stat.getVersion());

        zk.setData(path, "123456".getBytes(), -1);

        System.out.println(new String(zk.getData(path, true, stat)));
        System.out.println(stat.getCzxid() + "," + stat.getMzxid()+"，"+stat.getVersion());

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent event) {
        if(event.getState() == Event.KeeperState.SyncConnected) {
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                latch.countDown();
            }else if (event.getType() == Event.EventType.NodeDataChanged){
                try {
                    System.out.println(new String(zk.getData("/zk-book3", true, stat)));
                    System.out.println(stat.getCzxid() + "," + stat.getMzxid()+"，"+stat.getVersion());
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            latch.countDown();
        }
    }
}
