import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { StatCard } from '../StatCard';

const DummyIcon = ({ className }) => <svg data-testid="icon" className={className}></svg>;

describe('StatCard', () => {
  it('renders title, value and subtitle', () => {
    render(<StatCard title="Tests" value={42} subtitle="sub" />);
    expect(screen.getByText('Tests')).toBeInTheDocument();
    expect(screen.getByText('42')).toBeInTheDocument();
    expect(screen.getByText('sub')).toBeInTheDocument();
  });

  it('renders icon when provided', () => {
    render(<StatCard title="I" value={1} icon={DummyIcon} />);
    expect(screen.getByTestId('icon')).toBeInTheDocument();
  });
});
