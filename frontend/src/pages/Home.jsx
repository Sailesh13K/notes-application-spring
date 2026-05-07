import React, { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import NoteModal from "../components/NoteModal";
import NoteCard from "../components/NoteCard";
import { toast } from "react-toastify";
import api, { getErrorMessage } from "../api/client";

function Home() {
  const [isModelOpen, setModelOpen] = useState(false);
  const [notes, setNotes] = useState([]);
  const [currNote, setCurrNote] = useState(null);
  const [query, setQuery] = useState("");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);

  const fetchNotes = async ({ nextPage = page, search = query } = {}) => {
    try {
      setLoading(true);
      const endpoint = search.trim() ? "/api/notes/search" : "/api/notes";
      const { data } = await api.get(endpoint, {
        params: {
          page: nextPage,
          size: 9,
          ...(search.trim() ? { query: search.trim() } : {}),
        },
      });
      setNotes(data.data?.content || []);
      setPage(data.data?.number || 0);
      setTotalPages(data.data?.totalPages || 0);
    } catch (error) {
      toast.error(getErrorMessage(error, "Unable to load notes"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timeout = setTimeout(() => {
      fetchNotes({ nextPage: 0, search: query });
    }, 300);

    return () => clearTimeout(timeout);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [query]);

  const closeModal = () => setModelOpen(false);

  const onEdit = (note) => {
    setCurrNote(note);
    setModelOpen(true);
  };

  const addNote = async ({ title, description }) => {
    try {
      const response = await api.post("/api/notes", { title, description });
      if (response.data.success) {
        toast.success("Note added successfully");
        closeModal();
        fetchNotes({ nextPage: 0 });
      }
    } catch (error) {
      toast.error(getErrorMessage(error, "Unable to add note"));
    }
  };

  const deleteNote = async (id) => {
    try {
      const response = await api.delete(`/api/notes/${id}`);
      if (response.data.success) {
        toast.success("Note deleted successfully");
        fetchNotes();
      }
    } catch (error) {
      toast.error(getErrorMessage(error, "Unable to delete note"));
    }
  };

  const editNote = async ({ id, title, description }) => {
    try {
      const response = await api.put(`/api/notes/${id}`, { title, description });
      if (response.data.success) {
        toast.success("Note updated successfully");
        closeModal();
        fetchNotes();
      }
    } catch (error) {
      toast.error(getErrorMessage(error, "Unable to update note"));
    }
  };

  const goToPage = (nextPage) => {
    fetchNotes({ nextPage });
  };

  return (
    <div className="bg-gray-100 min-h-screen">
      <Navbar setQuery={setQuery} />

      <div className="grid gap-6 sm:grid-cols-2 md:grid-cols-3 p-4">
        {loading ? (
          <div className="flex justify-center items-center col-span-full mt-10">
            <div className="animate-spin rounded-full h-12 w-12 border-t-4 border-b-4 border-teal-500"></div>
          </div>
        ) : notes.length > 0 ? (
          notes.map((note) => (
            <NoteCard
              key={note.id}
              note={note}
              onEdit={onEdit}
              deleteNote={deleteNote}
            />
          ))
        ) : (
          <p className="m-4 col-span-full text-center text-gray-600">
            No notes found
          </p>
        )}
      </div>

      {totalPages > 1 && (
        <div className="flex justify-center items-center gap-4 pb-8">
          <button
            type="button"
            disabled={page === 0}
            onClick={() => goToPage(page - 1)}
            className="bg-gray-800 disabled:bg-gray-400 text-white px-4 py-2 rounded"
          >
            Previous
          </button>
          <span className="text-gray-700">
            Page {page + 1} of {totalPages}
          </span>
          <button
            type="button"
            disabled={page + 1 >= totalPages}
            onClick={() => goToPage(page + 1)}
            className="bg-gray-800 disabled:bg-gray-400 text-white px-4 py-2 rounded"
          >
            Next
          </button>
        </div>
      )}

      <button
        onClick={() => {
          setCurrNote(null);
          setModelOpen(true);
        }}
        className="fixed right-6 bottom-6 text-3xl bg-teal-500 hover:bg-teal-600 text-white font-bold p-4 rounded-full shadow-lg transition-transform transform hover:scale-105"
        title="Add Note"
      >
        +
      </button>

      {isModelOpen && (
        <NoteModal
          closeModal={closeModal}
          addNote={addNote}
          currNote={currNote}
          editNote={editNote}
        />
      )}
    </div>
  );
}

export default Home;
