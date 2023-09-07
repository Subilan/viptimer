package red.oases.viptimer.Utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import red.oases.viptimer.Objects.Distribution;

import java.util.UUID;

public class Sync {
    /**
     * 获取当前插件所在服务器对应的 instance id
     * 如果不存在则会创建，储存在 types.yml 内
     */
    public static String getInstanceId() {
        var instId = Files.types.getString("inst_id");

        if (instId == null) {
            instId = UUID.randomUUID().toString();
            Files.types.set("inst_id", instId);
            Files.saveTypes();
        }

        return instId;
    }

    /**
     * 将当前 <code>types.yml</code> 内容写入数据库。
     */
    public static void updateDistribution() {
        if (Data.updateDistribution(getInstanceId(), Files.types.saveToString())) {
            Logs.info("Successfully updated distribution.");
        } else {
            Logs.severe("Cannot create distribution of instance " + getInstanceId() + ".");
        }
    }

    /**
     * 将对应的 Distribution 标记为已接收，但此状态并不代表以后不会再接收。<br/>
     * 此函数将会
     * <ul>
     *     <li>在不存在 receipt 记录的时候创建，并将 recv_count 加一。</li>
     *     <li>如果已经存在，则直接将 recv_count 加一。</li>
     * </ul>
     */
    public static boolean markReceived(String distId) {
        if (Data.hasReceipt(distId)) {
            return Data.increaseRecvCount(distId);
        } else {
            if (Data.createReceipt(distId)) {
                return Data.increaseRecvCount(distId);
            } else {
                return false;
            }
        }
    }

    /**
     * 将 Distribution 对象包含的内容写入本地 <code>types.yml</code>。
     *
     * @param distribution 指定的对象
     */
    public static void writeDistribution(@NotNull Distribution distribution) {
        // Backup current instance id.
        var instanceId = getInstanceId();
        try {
            Files.types.loadFromString(distribution.dist_content());
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e.getMessage());
        }
        // Override incoming instance id.
        Files.types.set("inst_id", instanceId);
        Files.saveTypes();
    }
}
