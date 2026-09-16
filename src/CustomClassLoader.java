import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomClassLoader extends ClassLoader {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if ("TestModule".equals(name)) {
            return findClass(name);
        }
        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // Оскільки IDEA тримає файли в src/, вказуємо відповідний шлях
        File file = new File("src/" + name + ".class");

        if (!file.exists()) {
            throw new ClassNotFoundException("Не знайдено файл: " + file.getAbsolutePath());
        }

        try (InputStream inputStream = new FileInputStream(file);
             ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {

            int nextValue;
            while ((nextValue = inputStream.read()) != -1) {
                byteStream.write(nextValue);
            }

            byte[] classBytes = byteStream.toByteArray();
            return defineClass(name, classBytes, 0, classBytes.length);

        } catch (IOException e) {
            throw new ClassNotFoundException("Помилка читання файлу", e);
        }
    }
}