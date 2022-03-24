# Task №3 by Epam training center.

Task:
Разработать многопоточное приложение, использующее разделяемые
ресурсы. В приложении должна быть реализована функциональность,
определенная индивидуальным заданием.

Model:
Речной паром платформы и ее грузоподъемность ограничена. . Перевозит легковые и грузовые автомобили на другой берег Если паром заполнен, то оставшиеся . Площадь
автомобили выстраиваются в очередь.

Requirements:

➢ Любая сущность, желающая получить доступ к разделяемому ресурсу, должна быть
потоком.

➢ Программа может использовать возможности синхронизации, поставляемые библиотеками
java.util.concurrent, java.util.concurrent.atomic и java.util.concurrent.locks.

➢ Не использовать интерфейс BlockingQueue и другие ограниченно потокобезопасные
коллекции.

➢ Классы и другие сущности приложения должны быть грамотно структурированы по пакетам
и иметь отражающую их функциональность название.

➢ Использовать шаблон State для описания состояний объекта, если только этих состояний
больше двух.

➢ Для создания потоков разрешается использовать по возможности Callable.

➢ Запускать потоки с помощью ExecutorService.

➢ Вместо Thread.sleep использовать только возможности перечисления TimeUnit.

➢ Данные инициализации объектов считывать из файла. Данные в файле корректны.

➢ В приложении должен присутствовать потокобезопасный Singleton. При его создании
запрещено использовать enum.

➢ Для записи логов использовать Log4J2.

➢ Для вывода работы потоков использовать метод main.