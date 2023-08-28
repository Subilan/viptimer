package red.oases.viptimer.Utils;

public class Common {
    /**
     * 转换对应字符串为非负整数。如果转换失败，返回 0。
     *
     * @param target 待转换的字符串
     * @return 成功为对应整数，不成功为 0
     */
    public static int mustPositive(String target) {
        int result;
        try {
            result = Integer.parseInt(target);
        } catch (NumberFormatException e) {
            return 0;
        }
        return result;
    }
}
