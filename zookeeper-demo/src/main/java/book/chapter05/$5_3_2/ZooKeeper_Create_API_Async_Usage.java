package book.chapter05.$5_3_2;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author xinghailong at 2020-09-22 4:02 下午
 */
public class ZooKeeper_Create_API_Async_Usage implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zk = new ZooKeeper(
                "localhost:2181",
                5000,
                new ZooKeeper_Create_API_Async_Usage()
        );

        latch.await();

        // 异步接口不需要关心异常，实现Callback即可。
        zk.create(
                "/zk-test-ephemeral-",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL,
                new IStringCallback(),
                "I am context"
                );

        zk.create(
                "/zk-test-ephemeral-",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL,
                new IStringCallback(),
                "I am context"
        );

        zk.create(
                "/zk-test-ephemeral-",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL,
                new IStringCallback(),
                "I am context"
        );

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("receive watched event: " + event);
        if(event.getState() == Event.KeeperState.SyncConnected){
            latch.countDown();
        }
    }

    static class IStringCallback implements AsyncCallback.StringCallback {
        /**
         * 0 成功
         * -4 连接断开
         * -110 已存在
         * -112 会话过期
         *
         * @param rc   The return code or the result of the call.
         * @param path The path that we passed to asynchronous calls.
         * @param ctx  Whatever context object that we passed to asynchronous calls.
         * @param name The name of the znode that was created. On success, <i>name</i>
         *             and <i>path</i> are usually equal, unless a sequential node has
         *             been created.
         *
         */
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            System.out.println("Create path result: [" + rc + ", " + path + ", " + ctx + ", real path name: " + name);
        }
    }
}
