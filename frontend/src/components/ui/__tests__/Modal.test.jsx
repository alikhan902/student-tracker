import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { Modal } from '../Modal';

describe('Modal', () => {
  it('does not render when closed', () => {
    const { container } = render(<Modal isOpen={false} title="t">Hello</Modal>);
    expect(container.firstChild).toBeNull();
  });

  it('renders content and sets body overflow when open', () => {
    render(<Modal isOpen={true} title="My title">Content</Modal>);
    expect(screen.getByText('My title')).toBeInTheDocument();
    expect(screen.getByText('Content')).toBeInTheDocument();
    expect(document.body.style.overflow).toBe('hidden');
  });
});
