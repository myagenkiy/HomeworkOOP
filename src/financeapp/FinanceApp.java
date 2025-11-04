package financeapp;

import financeapp.exception.InvalidCredentialsException;
import financeapp.exception.UserNotFoundException;
import financeapp.model.User;
import financeapp.service.DataStorage;
import financeapp.service.FinanceService;
import financeapp.service.UserService;

import java.util.Map;
import java.util.Scanner;

public class FinanceApp {
    Map<String, User> users = DataStorage.loadUsers();
    private final UserService userService = new UserService(users);
    private final FinanceService financeService = new FinanceService();
    private final Scanner scanner = new Scanner(System.in);

    public void run () {
        System.out.println("\n--------Приложение для управления личными финансами--------");

        while (true) {
            if (!userService.isUserLoggedIn()) {
                showAuthMenu();
                handleAuthMenu();
            } else {
                showMainMenu();
                handleMainMenu();
            }
        }
    }

    private void showAuthMenu () {
        System.out.println("\nМеню авторизации");
        System.out.println("\n1. Регистрация");
        System.out.println("2. Вход");
        System.out.println("3. Выход");
        System.out.print("\nВыберите действие: ");
    }

    private void handleAuthMenu () {
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                handleRegistration();
                break;
            case "2":
                handleLogin();
                break;
            case "3":
                System.out.println("Выход из приложения...");
                DataStorage.saveData(users);
                System.exit(0);
                break;
            default:
                System.out.println("Неверный выбор");
        }
    }

    private void handleRegistration () {
        try {
            System.out.print("Введите уникальное имя пользователя: ");
            String username = scanner.nextLine().trim();

            System.out.print("Введите пароль: ");
            String password = scanner.nextLine().trim();

            userService.registerUser(username, password);
            System.out.println("Регистрация успешна");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка регистрации: " + e.getMessage());
        }
    }

    private void handleLogin () {
        try {
            System.out.print("Введите имя пользователя: ");
            String username = scanner.nextLine().trim();

            System.out.print("Введите пароль: ");
            String password = scanner.nextLine().trim();

            userService.authenticateUser(username, password);
            System.out.println("Вход выполнен успешно");
        } catch (InvalidCredentialsException | UserNotFoundException e) {
            System.out.println("Ошибка входа: " + e.getMessage());
        }
    }

    private void showMainMenu () {
        System.out.println("\nГлавное меню");
        System.out.println("\n1. Добавить доход");
        System.out.println("2. Добавить расход");
        System.out.println("3. Удалить доход или расход");
        System.out.println("4. Установить бюджет");
        System.out.println("5. Просмотр статистики");
        System.out.println("6. Выход");
        System.out.print("\nВыберите действие: ");
    }

    private void handleMainMenu () {
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                handleAddIncome();
                break;
            case "2":
                handleAddExpence();
                break;
            case "3":
                handleDelete();
                break;
            case "4":
                handleSetBudget();
                break;
            case "5":
                showStatistics();
                break;
            case "6":
                userService.logout();
                break;
            default:
                System.out.println("Неверный выбор");
        }
    }

    private void handleAddIncome () {
        try {
            System.out.print("Введите категорию дохода: ");
            String category = scanner.nextLine().trim();

            System.out.print("Введите сумму дохода: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            financeService.addIncome(userService.getCurrentUser(), category, amount);
            System.out.println("Доход добавлен");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void handleAddExpence () {
        try {
            System.out.print("Введите категорию расхода: ");
            String category = scanner.nextLine().trim();

            System.out.print("Введите сумму расхода: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            financeService.addExpence(userService.getCurrentUser(), category, amount);

            if (financeService.isBudgetExceeded(userService.getCurrentUser(), category)) {
                System.out.println("Бюджент по категории " + category + " превышен!");
            }

            System.out.println("Расход добавлен");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void handleDelete () {
        try {
            System.out.print("Введите категорию: ");
            String category = scanner.nextLine().trim();

            financeService.deleteIncomeOrExpence(userService.getCurrentUser(), category);
            System.out.println("Категория удалена");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void handleSetBudget () {
        try {
            System.out.print("Введите категорию: ");
            String category = scanner.nextLine().trim();

            System.out.print("Введите бюджет: ");
            double budget = Double.parseDouble(scanner.nextLine().trim());

            financeService.setBudget(userService.getCurrentUser(), category, budget);
            System.out.println("Бюджет установлен");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void showStatistics () {
        User user = userService.getCurrentUser();

        System.out.println("\nСтатистика");

        double totalIncome = financeService.getTotalIncome(user);
        double totalExpence = financeService.getTotalExpence(user);

        System.out.println("Общий доход: " + totalIncome);
        System.out.println("Общий расход: " + totalExpence);

        Map<String, Double> incomeByCategory = financeService.getIncomeByCategory(user);
        if (!incomeByCategory.isEmpty()) {
            System.out.println("Доходы по категориям: ");
            incomeByCategory.forEach((category, amount) ->
                    System.out.println(category + ": " + amount));
        }

        Map<String, Double> expensesByCategory = financeService.getExpensesByCategory(user);
        if (!expensesByCategory.isEmpty()) {
            System.out.println("Расходы по категориям:");
            expensesByCategory.forEach((category, amount) -> {
                    System.out.println(category + ": " + amount);
                    Double budget = user.getWallet().getBudget(category);
                    if (budget != null) {
                        double remaining = financeService.getBudgetRemaining(user, category);
                        System.out.println("Бюджет: " + budget + ", оставшийся бюджет: " + remaining);
                    }
            });
        }

        if (financeService.areExpensesExceedingIncome(user)) {
            System.out.println("Ваши расходы превышают доходы");
        }
    }

    public static void main(String[] args) {
        new FinanceApp().run();
    }
}
