all: ytb-down

ytb-down: YtbDown.scala
	scala-cli package --assembly $? . -f

clean:
	rm ytb-down

.PHONY: clean
