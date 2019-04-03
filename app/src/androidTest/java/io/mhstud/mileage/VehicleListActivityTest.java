package io.mhstud.mileage;

import io.mhstud.mileage.adapters.FakeAdapter;
import io.mhstud.mileage.tests.TestCase;

public class VehicleListActivityTest extends TestCase {
	protected VehicleListActivity activity;

	private final FakeAdapter mMockAdapter = new FakeAdapter();

	protected void setUp() throws Exception {
		super.setUp();

		activity = new VehicleListActivity(mMockAdapter);
	}

	public void testCanDelete() {
		mMockAdapter.setCount(1);
		assertFalse(activity.canDelete(0));

		mMockAdapter.setCount(2);
		assertTrue(activity.canDelete(0));
	}
}
