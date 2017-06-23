DOC_FOLDER=target/site
DOC_BRANCH=gh-pages

.PHONY: all doc test doc-folder doc-clean deploy

all: test

test:
	mvn test

deploy:
	mvn clean deploy -P release

doc: doc-clean
	mvn site
	git checkout ${DOC_BRANCH}
	git rm --ignore-unmatch -r *
	cp -r ./${DOC_FOLDER}/* ./
	git add .
	@echo ---------------------
	@echo 'Please check the contents and do'
	@echo '# git commit -m "updated docs" && git push manually'
	@echo 'then return to your original branch'

doc-folder:
	mkdir -p "${DOC_FOLDER}"

doc-clean: doc-folder
	rm -r "${DOC_FOLDER}"
