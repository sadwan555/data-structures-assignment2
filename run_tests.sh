#!/usr/bin/env bash
set -euo pipefail

build_dir="$(mktemp -d)"
trap 'rm -rf "$build_dir"' EXIT

find src -name '*.java' -print0 | xargs -0 javac -Xlint:unchecked -d "$build_dir"

for test_class in EdgeListTest AdjacencyListTest AdjacencyMatrixTest; do
  echo "--- $test_class ---"
  output="$(java -cp "$build_dir" "$test_class")"
  printf '%s\n' "$output"
  if grep -q 'incorrect' <<<"$output"; then
    echo "$test_class reported a failed check" >&2
    exit 1
  fi
done
