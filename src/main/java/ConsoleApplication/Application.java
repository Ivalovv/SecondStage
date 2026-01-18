package ConsoleApplication;

import dao.UserDao;
import spring.model.User;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Application {
    private final Scanner scanner;
    private UserDao userDao;

    public Application(UserDao userDao) {
        this.userDao = userDao;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        int choice;
        while (true) {
            showMenu();

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> create();
                case 2 -> read();
                case 3 -> update();
                case 4 -> delete();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Некорректный выбор, попробуйте снова");
            }
        }
    }

    private void showMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1 - Создать пользователя");
        System.out.println("2 - Прочитать пользователя");
        System.out.println("3 - Обновить пользователя");
        System.out.println("4 - Удалить пользователя");
        System.out.println("0 - Выход");
    }

    private void create() {
        String name = readName();
        String email = readEmail();

        int age = readAge();
        if (age == -1) return;

        User user = new User(name, email, age);
        userDao.create(user);

        System.out.println("Пользователь успешно создан: " + user);
    }

    private void read() {
        int id = readId();
        if (id == -1) return;

        User existingUser = userDao.read(id);

        if (existingUser == null) {
            System.out.println("Пользователя с таким id не существует");
        } else {
            System.out.println("Пользователь найден: " + existingUser);
        }

    }

    private void update() {
        int id = readId();
        if (id == -1) return;

        User existingUser = userDao.read(id);

        if (existingUser == null) {
            System.out.println("Пользователя с таким id не существует");
            return;
        }
        System.out.println("Пользователь найден: " + existingUser);

        boolean editing = true;
        while (editing) {
            showUpdateMenu();

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> existingUser.setName(readName());
                case 2 -> existingUser.setEmail(readEmail());
                case 3 -> {
                    int age = readAge();
                    if (age != -1) existingUser.setAge(age);
                }
                case 0 -> editing = false;
                default -> System.out.println("Некорректный выбор, попробуйте снова");
            }
            System.out.println("Текущий пользователь: " + existingUser);
        }
        userDao.update(existingUser);
        System.out.println("Пользователь успешно обновлён: " + existingUser);
    }

    private void delete() {
        int id = readId();
        if (id == -1) return;

        User existingUser = userDao.read(id);
        if (existingUser == null) {
            System.out.println("Пользователь с таким id не найден");
            return;
        }

        userDao.delete(id);
        System.out.println("Пользователь успешно удалён: " + existingUser);
    }

    private void showUpdateMenu() {
        System.out.println("Выберите поле для изменения:");
        System.out.println("1 - Имя");
        System.out.println("2 - Email");
        System.out.println("3 - Возраст");
        System.out.println("0 - Сохранить и выйти");
    }

    private int readId() {
        int id;

        System.out.print("Введите id пользователя: ");
        try {
            id = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Id должен быть числом");
            return -1;
        }

        return id;
    }

    private String readName() {
        System.out.print("Введите имя: ");
        return scanner.nextLine();
    }

    private String readEmail() {
        System.out.print("Введите email: ");
        return scanner.nextLine();
    }

    private int readAge() {
        int age;

        System.out.print("Введите возраст: ");
        try {
            age = scanner.nextInt();
            scanner.nextLine();

            if (age < 0) {
                System.out.println("Возраст не может быть отрицательным");
                return -1;
            }

        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Возраст должен быть числом");
            return -1;
        }
        return age;
    }
}
