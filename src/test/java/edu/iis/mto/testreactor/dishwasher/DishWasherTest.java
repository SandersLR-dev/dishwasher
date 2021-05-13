package edu.iis.mto.testreactor.dishwasher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.iis.mto.testreactor.dishwasher.engine.EngineException;
import edu.iis.mto.testreactor.dishwasher.pump.PumpException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.iis.mto.testreactor.dishwasher.engine.Engine;
import edu.iis.mto.testreactor.dishwasher.pump.WaterPump;

import java.util.List;

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
        FillLevel dummyLevel = FillLevel.FULL;
        WashingProgram anyProgram = WashingProgram.ECO;
        boolean anyFlag = true;
        ProgramConfiguration program = ProgramConfiguration.builder()
                                                           .withFillLevel(dummyLevel)
                                                           .withProgram(anyProgram)
                                                           .withTabletsUsed(anyFlag)
                                                           .build();
        when(door.closed())
               .thenReturn(false);
        RunResult result = washer.start(program);
        assertEquals(Status.DOOR_OPEN, result.getStatus());
    }

    @Test
    void programWithTabletsUsedAndDirtFilterCapacityOverloadShouldResultInFilterError() {
        FillLevel dummyLevel = FillLevel.FULL;
        WashingProgram anyProgram = WashingProgram.ECO;
        boolean anyFlag = true;
        ProgramConfiguration program = ProgramConfiguration.builder()
                .withFillLevel(dummyLevel)
                .withProgram(anyProgram)
                .withTabletsUsed(anyFlag)
                .build();
        when(door.closed())
                .thenReturn(true);
        RunResult result = washer.start(program);
        assertEquals(Status.ERROR_FILTER, result.getStatus());
    }

    @Test
    void runProgramShouldLockDoorsBeforeAnyOperation() {
        FillLevel dummyLevel = FillLevel.HALF;
        WashingProgram anyProgram = WashingProgram.ECO;
        boolean anyFlag = false;
        ProgramConfiguration program = ProgramConfiguration.builder()
                .withFillLevel(dummyLevel)
                .withProgram(anyProgram)
                .withTabletsUsed(anyFlag)
                .build();
        when(door.closed())
                .thenReturn(true);
        RunResult result = washer.start(program);

        verify(door).lock();

    }

    @Test
    void runProgramShouldUnlockDoorsAfterAllOperations() {
        FillLevel dummyLevel = FillLevel.HALF;
        WashingProgram anyProgram = WashingProgram.ECO;
        boolean anyFlag = false;
        ProgramConfiguration program = ProgramConfiguration.builder()
                .withFillLevel(dummyLevel)
                .withProgram(anyProgram)
                .withTabletsUsed(anyFlag)
                .build();
        when(door.closed())
                .thenReturn(true);
        RunResult result = washer.start(program);

        verify(door).unlock();
    }

    @Test
    void rinseProgramRunShouldRunOnlyRinseProgram() throws PumpException, EngineException {
        FillLevel dummyLevel = FillLevel.HALF;
        WashingProgram anyProgram = WashingProgram.RINSE;
        boolean anyFlag = false;
        ProgramConfiguration program = ProgramConfiguration.builder()
                .withFillLevel(dummyLevel)
                .withProgram(anyProgram)
                .withTabletsUsed(anyFlag)
                .build();
        when(door.closed())
                .thenReturn(true);
        RunResult result = washer.start(program);

        verify(pump,times(1)).pour(dummyLevel);
        verify(engine,times(1)).runProgram(List.of(2, 14));
        verify(pump,times(1)).drain();

    }

    @Test
    void programOtherThatnRinseShouldRunTheProgramAndRinseProgram() throws PumpException, EngineException {
        FillLevel dummyLevel = FillLevel.HALF;
        WashingProgram anyProgram = WashingProgram.ECO;
        boolean anyFlag = false;
        ProgramConfiguration program = ProgramConfiguration.builder()
                .withFillLevel(dummyLevel)
                .withProgram(anyProgram)
                .withTabletsUsed(anyFlag)
                .build();
        when(door.closed())
                .thenReturn(true);
        RunResult result = washer.start(program);

        verify(pump,times(2)).pour(dummyLevel);
        verify(engine,times(1)).runProgram(List.of(1, 90));
        verify(engine,times(1)).runProgram(List.of(2, 14));
        verify(pump,times(2)).drain();
    }

    @Test
    void engineRunProgramShouldPassProperCodes() {
        fail("unimplemented");
    }

    @Test
    void engineThrowsExceptionSholdReturnProgramError() throws EngineException {
        FillLevel dummyLevel = FillLevel.HALF;
        WashingProgram anyProgram = WashingProgram.RINSE;
        boolean anyFlag = false;
        ProgramConfiguration program = ProgramConfiguration.builder()
                .withFillLevel(dummyLevel)
                .withProgram(anyProgram)
                .withTabletsUsed(anyFlag)
                .build();
        doThrow(EngineException.class).when(engine).runProgram(List.of(2, 14));
        when(door.closed())
                .thenReturn(true);
        RunResult result = washer.start(program);




        assertThrows(EngineException.class,()->engine.runProgram(List.of(2, 14)));

        assertEquals(Status.ERROR_PROGRAM, result.getStatus());
    }

    @Test
    void pumpThrowsExceptionSholdReturnPumpError() throws PumpException {
        FillLevel dummyLevel = FillLevel.HALF;
        WashingProgram anyProgram = WashingProgram.RINSE;
        boolean anyFlag = false;
        ProgramConfiguration program = ProgramConfiguration.builder()
                .withFillLevel(dummyLevel)
                .withProgram(anyProgram)
                .withTabletsUsed(anyFlag)
                .build();
        doThrow(PumpException.class).when(pump).drain();
        when(door.closed())
                .thenReturn(true);
        RunResult result = washer.start(program);




        assertThrows(PumpException.class,()->pump.drain());

        assertEquals(Status.ERROR_PUMP, result.getStatus());
    }

}
