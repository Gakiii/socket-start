package mutithread;

public class testLabbda {
    public static void main(String[] args) {
        student a = new student();
        Runnable runnable = () -> {
            while (true) {
                System.out.println(Thread.currentThread().getName() + "-->" + "[" + a.age + "]");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        a.age += 1;
    }
}
class student {
    public int age = 0;
}
