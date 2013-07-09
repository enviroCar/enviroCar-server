module Jekyll
    class IncludeSchema < Liquid::Tag
        REPO_URL = "https://github.com/enviroCar/enviroCar-server.git"
        LANG = "json"
        SCHEMA_PATH = "rest/src/main/resources/schema"
        EXAMPLES_PATH = "_examples"
        @@pulled = false
        def initialize(tag_name, text, token)
            if @@pulled then
                pull = false
            else
                pull = true
                @@pulled = true
            end
            super
            case tag_name
            when "include_schema"
                @delegate = Jekyll::HighlightFileBlock.new(
                    "highlight_git",
                    "#{LANG} #{REPO_URL} #{SCHEMA_PATH}/#{text.strip!}.json pull=#{pull}",
                    token)
            when "include_example"
                @delegate = Jekyll::HighlightFileBlock.new(
                    "highlight_file",
                    "#{LANG} #{EXAMPLES_PATH}/#{text.strip!}.json",
                    token)
            else
                raise "Unrecognized tag name '#{tag_name}'"
            end
        end
        def render(context)
            @delegate.render context
        end
    end
end

Liquid::Template.register_tag('include_schema', Jekyll::IncludeSchema)
Liquid::Template.register_tag('include_example', Jekyll::IncludeSchema)
