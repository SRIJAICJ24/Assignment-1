import java.util.*;

public class Problem9 {
    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        long time;

        Transaction(int id, int amount, String merchant, String account, long time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.time = time;
        }

        public String toString() {
            return "{id:" + id + ", amount:" + amount + ", merchant:" + merchant + ", account:" + account + "}";
        }
    }

    public List<String> findTwoSum(List<Transaction> transactions, int target) {
        Map<Integer, Transaction> map = new HashMap<>();
        List<String> result = new ArrayList<>();
        for (Transaction t : transactions) {
            int need = target - t.amount;
            if (map.containsKey(need)) {
                result.add("(" + map.get(need).id + ", " + t.id + ")");
            }
            map.put(t.amount, t);
        }
        return result;
    }

    public List<String> findTwoSumWithTimeWindow(List<Transaction> transactions, int target, long windowMillis) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < transactions.size(); i++) {
            for (int j = i + 1; j < transactions.size(); j++) {
                if (transactions.get(i).amount + transactions.get(j).amount == target &&
                        Math.abs(transactions.get(i).time - transactions.get(j).time) <= windowMillis) {
                    result.add("(" + transactions.get(i).id + ", " + transactions.get(j).id + ")");
                }
            }
        }
        return result;
    }

    public List<List<Integer>> findKSum(int[] nums, int k, int target) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, k, target, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, int k, int target, int start, List<Integer> path, List<List<Integer>> result) {
        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        if (k == 0 || start == nums.length) return;

        for (int i = start; i < nums.length; i++) {
            path.add(nums[i]);
            backtrack(nums, k - 1, target - nums[i], i + 1, path, result);
            path.remove(path.size() - 1);
        }
    }

    public List<String> detectDuplicates(List<Transaction> transactions) {
        Map<String, Set<String>> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : transactions) {
            String key = t.amount + "|" + t.merchant;
            map.putIfAbsent(key, new HashSet<>());
            map.get(key).add(t.account);
        }

        for (Map.Entry<String, Set<String>> e : map.entrySet()) {
            if (e.getValue().size() > 1) {
                result.add(e.getKey() + " -> " + e.getValue());
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Problem9 p = new Problem9();
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1, 500, "Store A", "acc1", 1000),
                new Transaction(2, 300, "Store B", "acc2", 1500),
                new Transaction(3, 200, "Store C", "acc3", 2000),
                new Transaction(4, 500, "Store A", "acc4", 2500)
        );

        System.out.println(p.findTwoSum(transactions, 500));
        System.out.println(p.findTwoSumWithTimeWindow(transactions, 700, 2000));
        System.out.println(p.detectDuplicates(transactions));
        System.out.println(p.findKSum(new int[]{500, 300, 200}, 3, 1000));
    }
}