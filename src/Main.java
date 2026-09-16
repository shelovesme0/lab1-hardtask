import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Шлях до файлу відносно кореня проєкту IDEA
        File sourceFile = new File("src/TestModule.java");
        long lastModified = 0;

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.err.println("Компілятор не знайдено! Використовуйте JDK.");
            return;
        }

        System.out.println("Запущено моніторинг файлу TestModule.java...");

        while (true) {
            if (sourceFile.exists() && sourceFile.lastModified() > lastModified) {
                lastModified = sourceFile.lastModified();
                System.out.println("\n[!] Зміни виявлено. Перекомпіляція...");

                int result = compiler.run(null, null, null, sourceFile.getPath());

                if (result == 0) {
                    try {
                        CustomClassLoader loader = new CustomClassLoader();
                        Class<?> clazz = loader.loadClass("TestModule");
                        Object t = clazz.getDeclaredConstructor().newInstance();
                        System.out.println("Результат: " + t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Помилка компіляції!");
                }
            }
            Thread.sleep(2000);
        }
    }
}