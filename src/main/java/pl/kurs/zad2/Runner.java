package pl.kurs.zad2;

import org.w3c.dom.ls.LSOutput;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

public class Runner {
    public static void main(String[] args) throws InvlaidPeselException {

        System.out.println("Podaj swoje imie:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Optional<String> optionalName = Optional.ofNullable(name);
        int lenght = optionalName.map(String::length)
                .orElse(0);
        System.out.println("Długośc imienia to: " + lenght);

        System.out.println("Podaj pesel");

        String pesel = scanner.nextLine();
        System.out.println(getDateFromPesel(pesel));
    }


    private static LocalDate getDateFromPesel(String pesel) throws InvlaidPeselException {

        pesel = Optional.ofNullable(pesel)
                .filter(x -> x.matches("\\d{11}"))
                .orElseThrow(() -> new InvlaidPeselException("Podany pesel jest błędny"));

        return LocalDate.of(
                Integer.parseInt("19" + pesel.substring(0, 2)),
                Integer.parseInt(pesel.substring(2, 4)),
                Integer.parseInt(pesel.substring(4, 6))
        );


    }
}