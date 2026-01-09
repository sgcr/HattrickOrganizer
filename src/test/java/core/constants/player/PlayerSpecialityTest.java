package core.constants.player;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerSpecialityTest {

    private static final int NO_SPECIALITY = 0;
    private static final int TECHNICAL = 1;
    private static final int QUICK = 2;
    private static final int POWERFUL = 3;
    private static final int UNPREDICTABLE = 4;
    private static final int HEAD = 5;
    private static final int REGAINER = 6;
    private static final int SUPPORT = 8;

    private static final int[] VALUES = {
            NO_SPECIALITY,
            TECHNICAL,
            QUICK,
            POWERFUL,
            UNPREDICTABLE,
            HEAD,
            REGAINER,
            SUPPORT
    };

    @Test
    void constant_NO_SPECIALITY() {
        assertThat(PlayerSpeciality.NO_SPECIALITY).isEqualTo(0);
    }

    @Test
    void constant_TECHNICAL() {
        assertThat(PlayerSpeciality.TECHNICAL).isEqualTo(1);
    }

    @Test
    void constant_QUICK() {
        assertThat(PlayerSpeciality.QUICK).isEqualTo(2);
    }

    @Test
    void constant_POWERFUL() {
        assertThat(PlayerSpeciality.POWERFUL).isEqualTo(3);
    }

    @Test
    void constant_UNPREDICTABLE() {
        assertThat(PlayerSpeciality.UNPREDICTABLE).isEqualTo(4);
    }

    @Test
    void constant_HEAD() {
        assertThat(PlayerSpeciality.HEAD).isEqualTo(5);
    }

    @Test
    void constant_REGAINER() {
        assertThat(PlayerSpeciality.REGAINER).isEqualTo(6);
    }

    @Test
    void constant_SUPPORT() {
        assertThat(PlayerSpeciality.SUPPORT).isEqualTo(8);
    }

    @Test
    void values() {
        assertThat(PlayerSpeciality.values()).isEqualTo(VALUES);
    }

    @Test
    void testToString() {
    }

    @Test
    void getWeatherEffect() {
    }
}