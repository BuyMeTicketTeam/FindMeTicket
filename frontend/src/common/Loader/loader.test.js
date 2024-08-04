import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import Loader from "./Loader";

test("renders Loader component", () => {
  render(<Loader />);
  const loader = screen.getByTestId("loader");
  expect(loader).toBeInTheDocument();
});

jest.useFakeTimers();

test("changes the animation text after a certain interval", async () => {
  const { getByTestId } = render(<Loader />);
  const loaderElement = getByTestId("loader");

  // Initial animation class
  expect(loaderElement.querySelector(".loader__inner-text")).toHaveClass(
    "first"
  );

  // Fast-forward time by 3000 milliseconds to simulate the interval
  jest.advanceTimersByTime(3000);

  // Wait for the next tick of the event loop to allow the state to update
  await waitFor(() => {
    expect(loaderElement.querySelector(".loader__inner-text")).toHaveClass(
      "second"
    );
  });
});
