package co.axelrod.webnsfw.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 27.01.2018.
 */

@Getter
@Setter
@AllArgsConstructor
public class Result {
    private Float score;
    private String file;

    @Override
    public String toString() {
        return String.format("%.20f", score) + " " + file;
    }
}
