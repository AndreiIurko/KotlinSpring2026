# Project Troubleshooting Guide

This document contains solutions to common issues encountered during development and CI/CD configuration.

## 1. Git Submodule Errors

### Issue: Protocol Mismatch (HTTPS vs SSH)
If `.gitmodules` uses HTTPS but the error shows `git@github.com` (SSH), your local config is cached or globally overridden.

**Fix:**
```bash
# 1. Sync configuration to match .gitmodules
git submodule sync --recursive

# 2. Update submodules
git submodule update --init --recursive --force

# 3. Check for global overrides (if step 2 fails)
git config --global --unset url."git@github.com:".insteadOf