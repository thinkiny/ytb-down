all: ytb-down

ytb-down: YtbDown.scala
	scala-cli package $? . -f

clean:
	rm ytb-down

.PHONY: clean
