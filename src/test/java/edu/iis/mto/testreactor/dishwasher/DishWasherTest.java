package edu.iis.mto.testreactor.dishwasher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.iis.mto.testreactor.dishwasher.engine.Engine;
import edu.iis.mto.testreactor.dishwasher.pump.WaterPump;

@ExtendWith(MockitoExtension.class)
class DishWasherTest {

    @Mock
    private WaterPump pump;
    @Mock
    private Engine engine;
    @Mock
    private DirtFilter fiter;
    @Mock
    private Door door;
    private DishWasher washer;

    @BeforeEach
    void setup() {
        washer = new DishWasher(pump, engine, fiter, door);
    }

    @Test
    void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }

    @Test
    void startingProgramWithDoorsOpenShouldResultInDoorError() {
        fail("unimplemented");
    }

    @Test
    void programWithTabletsUsedAndDirtFilterCapacityOverloadShouldResultInFilterError() {
        fail("unimplemented");
    }

    @Test
    void runProgramShouldLockDoorsBeforeAnyOperation() {
        fail("unimplemented");
    }

    @Test
    void runProgramShouldUnlockDoorsAfterAllOperations() {
        fail("unimplemented");
    }

    @Test
    void rinseProgramRunShouldRunOnlyRinseProgram() {
        fail("unimplemented");
    }

    @Test
    void programOtherThatnRinseShouldRunTheProgramAndRinseProgram() {
        fail("unimplemented");
    }

    @Test
    void engineRunProgramShouldPassProperCodes() {
        fail("unimplemented");
    }

    @Test
    void engineThrowsExceptionSholdReturnProgramError() {
        fail("unimplemented");
    }

    @Test
    void pumpThrowsExceptionSholdReturnPumpError() {
        fail("unimplemented");
    }

}
