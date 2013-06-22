#!/bin/sh -e
repo_url=https://github.com/car-io/car.io-server.git
deploy_dir=_deploy


if [ ! -d $deploy_dir ]; then
    git clone $repo_url -b gh-pages $deploy_dir
else
    git pull $deploy_dir
fi

rm -rf $deploy_dir/*

jekyll build -d $deploy_dir


pushd $deploy_dir
git add --ignore-removal .
git add --update :/
git ci
git push origin gh-pages
popd

