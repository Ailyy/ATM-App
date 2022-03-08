package yechao;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATMSystem {
    public static void main(String[] args) {
        // 1、准备系统需要的容器对象，用于存储账户对象
        ArrayList<Account> accounts = new ArrayList<>();

        // 2、准备系统的首页：注册账户，登录账户
        showMain(accounts);
    }

    public static void showMain(ArrayList<Account> accounts){
        System.out.println("=================欢迎进入ATM系统===================");
        Scanner in = new Scanner(System.in);

        while (true){
            System.out.println("请输入您要进行的操作命令：");
            System.out.println("1、注册账户");
            System.out.println("2、登录账户");
            System.out.print("请输入命令对应的数字：");
            int command = in.nextInt();
            switch (command){
                case 1:
                    // 注册账户
                    register(accounts, in);
                    break;
                case 2:
                    // 登录账户
                    login(accounts, in);
                    break;
                default:
                    System.out.println("您输入的命令有误！");
            }
        }
    }

    /**
     * 登录账户功能
     * @param accounts
     * @param in
     */

    private static void login(ArrayList<Account> accounts, Scanner in) {
        if (accounts.size() == 0){
            System.out.println("当前系统中无任何账户，您需要先注册！");
            return;
        }

        while (true){
            // 让用户录入登录的卡号
            System.out.println("请输入登录的账户卡号：");
            String cardId = in.next();
            Account acc = getAccountByCardId(cardId, accounts);
            if (acc != null){
                while (true){
                    System.out.println("请输入登录密码：");
                    String passWord = in.next();
                    if (passWord.equals(acc.getPassWord())){
                        // 密码正确，登录成功
                        System.out.println("欢迎" + acc.getUserName() + "进入系统，您的卡号是：" + acc.getCardId());
                        // 展示操作页面
                        showUserCommand(in, acc, accounts);
                        return;
                    }else {
                        // 输入密码错误，要求重新输入
                        System.out.println("您输入的密码有误，请重新输入！");
                    }
                }
            }else {
                System.out.println("您输入的卡号不存在，请重新输入：");
            }
        }
    }

    private static void showUserCommand(Scanner in, Account acc, ArrayList<Account> accounts) {
        System.out.println("===========欢迎进入操作界面===========");
        while (true){
            System.out.println("请输入要进行的操作命令：");
            System.out.println("1、查询账户");
            System.out.println("2、存款");
            System.out.println("3、取款");
            System.out.println("4、转账");
            System.out.println("5、修改密码");
            System.out.println("6、退出");
            System.out.println("7、注销账户");
            System.out.print("请输入要执行的命令：");
            int command = in.nextInt();
            switch (command){
                case 1:
                    // 查询账户
                    showAccount(acc);
                    break;
                case 2:
                    // 存款
                    depositMoney(acc, in);
                    break;
                case 3:
                    // 取款
                    drawMoney(acc, in);
                    break;
                case 4:
                    // 转账
                    transferMoney(acc, in, accounts);
                    break;
                case 5:
                    // 修改密码
                    updatePassWord(in, acc);
                    return;
                case 6:
                    // 退出
                    System.out.println("欢迎下次光临！");
                    return;
                case 7:
                    // 注销账户
                    if (deleteAccount(acc, in, accounts)){
                        // 注销成功
                        return;// 结束当前方法
                    }else {
                        break;
                    }
                default:
                    System.out.println("您输入的命令有误，请重新输入！");
            }
        }
    }

    /**
     * 注销账户
     * @param acc 当前登录账户
     * @param in 扫描器
     * @param accounts 所有账户集合
     */
    private static boolean deleteAccount(Account acc, Scanner in, ArrayList<Account> accounts) {
        System.out.println("===============用户注销===============");
        System.out.println("您真的要注销账户吗？y/n");
        String rs = in.next();
        switch (rs){
            case "y":
                // 真正开始注销
                if (acc.getMoney() > 0){
                    System.out.println("您的账户钱还没有取完，无法注销！");
                }else {
                    accounts.remove(acc);
                    System.out.println("注销成功！");
                    return true;
                }
                break;
            default:
                System.out.println("当前账户继续保留！");
        }
        return false;
    }

    /**
     * 修改密码
     * @param in 扫描器
     * @param acc 当前登录账户
     */
    private static void updatePassWord(Scanner in, Account acc) {
        System.out.println("===============欢迎进入修改密码操作===============");
        while (true) {
            System.out.println("请输入当前的密码：");
            String passWord = in.next();
            // 判断密码是否正确
            if (acc.getPassWord().equals(passWord)){
                // 密码正确
                while (true) {
                    System.out.println("请输入新密码：");
                    String newPassWord = in.next();
                    System.out.println("请确认新密码：");
                    String okPassWord = in.next();
                    if (newPassWord.equals(okPassWord)){
                        acc.setPassWord(newPassWord);
                        System.out.println("恭喜你，密码修改成功！");
                        return;// 结束方法
                    }else {
                        System.out.println("对不起，您输入的2次密码不一致！");
                    }
                }
            }else {
                System.out.println("对不起，您输入的密码有误！");
            }
        }
    }

    /**
     * 转账操作
     * @param acc 自己的账户
     * @param in 扫描器
     * @param accounts 所有账户
     */
    private static void transferMoney(Account acc, Scanner in, ArrayList<Account> accounts) {
        System.out.println("===============欢迎进入转账操作===============");
        // 判断是否足够2个账户
        if (accounts.size() < 2){
            System.out.println("对不起，系统中不足2个账户，无法转账！");
            return;
        }

        // 判断自己的账户是否有钱
        if (acc.getMoney() == 0){
            System.out.println("对不起，您的账户余额为0，无法转账！");
            return;
        }

        while (true){
            // 正式开始转账
            System.out.println("请输入对方账户的卡号：");
            String cardId = in.next();

            // 这个卡号不能是自己的卡号
            if (cardId.equals(acc.getCardId())){
                System.out.println("对不起，您不可以给自己转账！");
                continue;// 结束当前循环，进入下一次死循环
            }

            // 判断这个卡号是否存在，根据卡号去查找对方账户对象
            Account account = getAccountByCardId(cardId, accounts);
            if (account == null){
                System.out.println("您输入的账户不存在！");
            }else {
                // 这个账户对象存在了，证明他的姓氏
                String userName = account.getUserName();
                String tip = "*" + userName.substring(1);
                System.out.println("请输入[" + tip + "]的姓氏：");
                String preName = in.next();

                // 判断姓氏是否正确
                if (userName.startsWith(preName)){
                    while (true) {
                        // 认证通过，开始转账
                        System.out.println("请您输入要转账的金额：");
                        double money = in.nextDouble();
                        // 判断账户余额是否足够
                        if (money > acc.getMoney()){
                            System.out.println("对不起，您的账户余额不足！您最多可以转：" + acc.getMoney());
                        }else {
                            acc.setMoney(acc.getMoney() - money);
                            account.setMoney(account.getMoney() + money);
                            System.out.println("转账成功！您的账户还剩余额：" + acc.getMoney());
                            return;// 转账成功，结束转账方法
                        }
                    }
                }else {
                    System.out.println("对不起，您输入的信息有误！");
                }
            }
        }
    }

    /**
     * 取钱操作
     * @param acc 当前账户对象
     * @param in 扫描器
     */
    private static void drawMoney(Account acc, Scanner in) {
        System.out.println("===============欢迎进入取钱操作===============");
        // 判断金额是否足够100
        if (acc.getMoney() < 100){
            System.out.println("对不起，您的余额不足，请先存钱！");
            return;
        }

        while (true) {
            // 提示用户输入取钱金额
            System.out.println("请输入取款金额：");
            double money = in.nextDouble();

            // 判断金额是否满足取钱要求
            if (money > acc.getQuotaMoney()){
                System.out.println("对不起，您的取款金额超过当次限额，您每次最多可取：" + acc.getQuotaMoney());
            }else {
                // 没有超过限额
                // 判断是否超过账户余额
                if (money > acc.getMoney()){
                    System.out.println("余额不足！您的当前余额为：" + acc.getMoney());
                }else {
                    // 正式取钱
                    System.out.println("恭喜你，取款" + money + "成功！");
                    // 更新余额
                    acc.setMoney(acc.getMoney() - money);
                    showAccount(acc);
                    return;// 结束取钱方法
                }
            }
        }
    }

    /**
     * 存钱操作
     * @param acc 当前账户对象
     * @param in 扫描器
     */
    private static void depositMoney(Account acc, Scanner in) {
        System.out.println("===============欢迎进入存钱操作===============");
        System.out.println("请输入您要存入的金额：");
        // 用户输入存款金额
        double money = in.nextDouble();

        // 账户原来的金额加上存入的金额
        acc.setMoney(acc.getMoney() + money);

        System.out.println("恭喜你，存款成功，你的账户信息为：");
        showAccount(acc);
    }

    public static void showAccount(Account acc){
        System.out.println("===============当前账户详情===============");
        System.out.println("卡号：" + acc.getCardId());
        System.out.println("姓名：" + acc.getUserName());
        System.out.println("余额：" + acc.getMoney());
        System.out.println("当次限额：" + acc.getQuotaMoney());
    }

    /**
     * 用户注册账户功能
     * @param accounts 账户的集合对象
     * @param in
     */
    public static void register(ArrayList<Account> accounts, Scanner in){
        System.out.println("==========用户注册功能==========");
        // 键盘录入账户名称，账户密码，确认密码
        System.out.println("请输入注册账户名称：");
        String userName = in.next();
        String passWord = "";
        while (true){
            System.out.println("请输入账户密码：");
            passWord = in.next();
            System.out.println("请再次确认密码：");
            String okPassWord = in.next();
            // 判断两次密码是否一致
            if (okPassWord.equals(passWord)){
                break;
            }else {
                System.out.println("两次输入的密码不一致，请重新输入：");
            }
        }

        System.out.println("请输入当次限额：");
        double quotaMoney = in.nextDouble();

        // 生成账户卡号，卡号必须为系统自动生成的8位数字，且不能重复
        String cardId = createCardId(accounts);

        // 创建一个账户对象封装账户信息
        Account account = new Account(cardId, userName, passWord, quotaMoney);

        // 把账户加入到集合中去
        accounts.add(account);
        System.out.println("注册成功！您的卡号是：" + account.getCardId());
    }

    public static String createCardId(ArrayList<Account> accounts){
        while (true){
            // 生成八位随机数代表卡号
            Random r = new Random();
            String cardId = "";
            for (int i = 0; i < 8; i++) {
                cardId += r.nextInt(10);
            }

            // 判断卡号是否重复了
            Account acc = getAccountByCardId(cardId, accounts);
            if (acc == null){
                return cardId;
            }
        }
    }

    public static Account getAccountByCardId(String cardId, ArrayList<Account> accounts){
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if (acc.getCardId().equals(cardId)){
                return acc;
            }
        }
        return null;
    }
}
