package by.andrew;

public class Constants {
    public final static String ENTER_LOGIN_AND_PASSWORD_RU =
            "Введите ваш login/email и пароль учётной записи Куфар," +
                    " в формате:\n\nYourLogin YourPassword123";

    public final static String AUTH = "https://www.kufar.by/l/api/login/v2/auth/signin?token_type=user";
    public final static String GET_ALL_ADS = "https://www.kufar.by/account/my_ads/published";
    public final static String ACCAUNT_INFO = "https://www.kufar.by/react/api/user?apiName=account_info";
    public final static String SUCCESS_AUTH = "Вход выполнен ✅";
    public static final String ERROR_AUTH = "Вход не выполнен ⛔ Проверьте свой email/password";
}
