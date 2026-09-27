package de.petanqueturniermanager.algorithmen.common.ruleset.matchup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class DefaultCoreTest {

    @Test 
    public void simpleTestcase()
    {
        IMatchup    sut = new DefaultCoreRule();
        assertThat(sut.isRuleEffective()).isEqualTo(true);
    }
}
