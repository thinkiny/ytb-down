all: ytb-down

ytb-down: *.scala
	scala-cli package --assembly $? . -f

clean:
	rm ytb-down

.PHONY: clean
