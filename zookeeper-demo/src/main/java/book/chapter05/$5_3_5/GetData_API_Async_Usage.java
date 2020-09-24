package book.chapter05.$5_3_5;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author xinghailong at 2020-09-22 4:33 下午
 */
public class GetData_API_Async_Usage implements Watcher {
    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String path = "/zk-book4";

        zk = new ZooKeeper(
                "localhost:2181",
                5000,
                new GetData_API_Async_Usage()
        );

        latch.await();

        zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.getData(path, true, new IDataCallback(), null);
        zk.setData(path, "123456".getBytes(), -1);
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent event) {
        if(event.getState() == Event.KeeperState.SyncConnected) {
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                latch.countDown();
            }else if (event.getType() == Event.EventType.NodeDataChanged){
                zk.getData(event.getPath(), true, new IDataCallback(), null);
            }
            latch.countDown();
        }
    }

    static class IDataCallback implements AsyncCallback.DataCallback {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            System.out.println(rc+"， "+path+", "+new String(data));
            System.out.println(stat.getCzxid()+", "+stat.getMzxid()+", "+stat.getVersion());
        }
    }
}
