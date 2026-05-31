import '@testing-library/jest-dom';

// Prevent jsdom navigation errors during tests by stubbing navigation
//-related behavior and preventing real navigation from <a> clicks.
const noop = () => {};

try {
	if (typeof window !== 'undefined') {
		// Best-effort: don't throw if properties are non-configurable.
		try { window.history.pushState = window.history.pushState || noop; } catch (e) {}
		try { window.history.replaceState = window.history.replaceState || noop; } catch (e) {}
		try { if (window.location) { window.location.assign = window.location.assign || noop; } } catch (e) {}
		try { if (window.location) { window.location.replace = window.location.replace || noop; } } catch (e) {}
	}
} catch (e) {}

// Prevent anchor clicks from triggering jsdom navigation (Not implemented error).
if (typeof HTMLAnchorElement !== 'undefined') {
	const originalClick = HTMLAnchorElement.prototype.click;
	HTMLAnchorElement.prototype.click = function (...args) {
		const href = this.getAttribute && this.getAttribute('href');
		if (href && !href.startsWith('#') && href !== 'javascript:void(0)') {
			// swallow navigation in tests
			return undefined;
		}
		return originalClick.apply(this, args);
	};
}

// Also prevent programmatic click navigation via delegated events
if (typeof document !== 'undefined') {
	document.addEventListener('click', (e) => {
		try {
			const target = e.target;
			if (target && target.tagName === 'A') {
				const href = target.getAttribute && target.getAttribute('href');
				if (href && !href.startsWith('#') && href !== 'javascript:void(0)') {
					e.preventDefault();
				}
			}
		} catch (err) {
			// ignore
		}
	}, true);
}

// Stub URL.createObjectURL / revokeObjectURL used in download tests
try {
	if (typeof window !== 'undefined' && typeof window.URL !== 'undefined') {
		try {
			if (typeof window.URL.createObjectURL !== 'function') {
				window.URL.createObjectURL = () => 'blob:stub';
			}
		} catch (e) {}
		try {
			if (typeof window.URL.revokeObjectURL !== 'function') {
				window.URL.revokeObjectURL = () => {};
			}
		} catch (e) {}
	}
} catch (e) {}
